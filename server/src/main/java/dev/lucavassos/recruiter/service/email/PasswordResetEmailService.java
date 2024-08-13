package dev.lucavassos.recruiter.service.email;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PasswordResetEmailService extends EmailService {

    @Value("${client.reset-password-url}")
    private String resetPasswordUrl;

    @Value("${password.reset.token.expirationInSeconds}")
    private Integer expirationInSeconds;

    public void sendPasswordResetEmail(String to, String name, String resetToken) throws MessagingException {
        String htmlContent = "Hi %s, <br/><br/> "
                + "<p>You are receiving this email since you requested to reset your password. "
                + "If it wasn't you, please ignore this email altogether.<br/><br/>"
                + "<p>Click the following link below to reset your password: %s%s%s </p><br/>"
                + "<h4>The reset link is valid for %d minutes.</h4><br/><br/>"
                + "With regards,<br/>Recruiter Platform";
        htmlContent = String.format(htmlContent, name, super.clientBaseUrl, this.resetPasswordUrl, resetToken, expirationInSeconds / 60);
        String subject = "Password reset for %s - The Recruiter Platform";
        subject = String.format(subject, name);
        super.send(to, subject, htmlContent);
    }
}
