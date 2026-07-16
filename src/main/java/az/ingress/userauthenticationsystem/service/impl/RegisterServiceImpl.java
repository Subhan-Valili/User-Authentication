package az.ingress.userauthenticationsystem.service.impl;

import az.ingress.userauthenticationsystem.dto.RegisterRequest;
import az.ingress.userauthenticationsystem.dto.VerifyRequest;
import az.ingress.userauthenticationsystem.entity.Role;
import az.ingress.userauthenticationsystem.entity.User;
import az.ingress.userauthenticationsystem.exception.exc.*;
import az.ingress.userauthenticationsystem.repository.jpa.RoleRepository;
import az.ingress.userauthenticationsystem.repository.jpa.UserRepository;
import az.ingress.userauthenticationsystem.service.EmailService;
import az.ingress.userauthenticationsystem.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String preRegisterUser(RegisterRequest request) {
        log.info("Registration attempt initiated for username: {}", request.username());

        if (userRepository.findByUsername(request.username()).isPresent()) {
            log.info("Username already in use: {}", request.username());
            throw new UserAlreadyExists();
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            log.info("Email already in use: {}", request.email());
            throw new EmailExists();
        }

        String email = request.email();
        String otpKey = "reg:otp:" + email;
        String userKey = "reg:user:" + email;

        Boolean hasActiveOtp = redisTemplate.hasKey(otpKey);
        if (Boolean.TRUE.equals(hasActiveOtp)) {
            throw new OtpSent();
        }

        String otp = emailService.generateOTP();

        redisTemplate.opsForValue().set(userKey, request, 15, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(otpKey, otp, 3, TimeUnit.MINUTES);

        emailService.otpEmail(email, otp);

        return "OTP kod göndərildi!";
    }

    @Override
    public void verifyAndRegisterUser(VerifyRequest request) {
        String email = request.email();
        String otpKey = "reg:otp:" + email;
        String userKey = "reg:user:" + email;
        String attemptKey = "reg:attempts:" + email;

        String cachedOtp = (String) redisTemplate.opsForValue().get(otpKey);
        if (cachedOtp == null) {
            throw new OtpExpired();
        }

        Integer attempts = (Integer) redisTemplate.opsForValue().get(attemptKey);
        if (attempts != null && attempts >= 3) {
            redisTemplate.delete(otpKey);
            redisTemplate.delete(userKey);
            redisTemplate.delete(attemptKey);
            throw new OtpExpired();
        }

        if (!cachedOtp.equals(request.otp())) {
            Long currentAttempts = redisTemplate.opsForValue().increment(attemptKey, 1);

            if (currentAttempts != null && currentAttempts == 1) {
                redisTemplate.expire(attemptKey, 3, java.util.concurrent.TimeUnit.MINUTES);
            }

            int remaining = 3 - (currentAttempts != null ? currentAttempts.intValue() : 1);

            if (remaining <= 0) {
                redisTemplate.delete(otpKey);
                redisTemplate.delete(userKey);
                redisTemplate.delete(attemptKey);
                throw new OtpExpired();
            }

            throw new OtpDontMatch(remaining);
        }

        RegisterRequest registerRequest = (RegisterRequest) redisTemplate.opsForValue().get(userKey);
        if (registerRequest == null) {
            throw new SessionExpired();
        }

        User newUser = User.builder()
                .email(registerRequest.email())
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();

        if (newUser.getRoles() == null) {
            newUser.setRoles(new LinkedHashSet<>());
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> {
                    log.info("Role not found");
                    return new RoleNotFound("ROLE_USER");
                });
        newUser.getRoles().add(role);

        userRepository.save(newUser);

        String subject = "Welcome to Authentication System";
        String text = String.format("%s Xos Gelmisiniz!", registerRequest.username());
        emailService.sendSimpleEmail(request.email(), subject, text);

        redisTemplate.delete(userKey);
        redisTemplate.delete(otpKey);
        redisTemplate.delete(attemptKey);
    }


}
