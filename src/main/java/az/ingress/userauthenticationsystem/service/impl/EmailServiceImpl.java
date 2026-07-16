package az.ingress.userauthenticationsystem.service.impl;

import az.ingress.userauthenticationsystem.service.EmailService;

import java.security.SecureRandom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom; // Spring artıq bizim config-dəki SecureRandom-u bura ötürəcək

    @Async
    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email successfully sent to {}", to);
        } catch (Exception e) {
            log.error("Email sending failed to '{}': {}", to, e.getMessage());
        }
    }

    @Async
    @Override
    public void otpEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            String subject = "OTP Kode";
            String text = String.format("Bu kodu hec kim ile paylasmayin %s.", otp);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("OTP successfully sent to {}", to);
        } catch (Exception e) {
            log.error("OTP sending failed to '{}': {}", to, e.getMessage());
        }
    }

    @Override
    public String generateOTP() {
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
}