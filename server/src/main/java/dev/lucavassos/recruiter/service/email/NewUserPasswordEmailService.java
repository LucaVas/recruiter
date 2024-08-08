package dev.lucavassos.recruiter.service.email;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewUserPasswordEmailService extends EmailService {

    public void sendEmailWithPassword(String to, String name, String creatorEmail, String password) throws MessagingException {
        String htmlContent = "Hi %s, <br/><br/>"
                + "<p>You are receiving this email because a new account was created for you by %s. "
                + "If you don't recognize this action, please ignore this email altogether.<br/>"
                + "<p>Click the following link to login: %s </p>"
                + "<p>Your access credentials:</p>"
                + "<ul><li>Username: %s</li><br/>"
                + "<li>Password: <b>%s</b></li></ul>"
                + "<h4>We strongly recommend you to change your password as first action after logging in.</h4>"
                + "<p>You will be able to to so in your settings.</p><br/>"
                + "With regards,<br/>Recruiter Platform";
        htmlContent = String.format(htmlContent, name, creatorEmail, super.clientBaseUrl, to, password);
        String subject = "New account created for %s - The Recruiter Platform".formatted(name);
        super.send(to, subject, htmlContent);
    }
}
