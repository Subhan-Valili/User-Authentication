package az.ingress.userauthenticationsystem.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void otpEmail(String to, String otp);
    String generateOTP();
}
