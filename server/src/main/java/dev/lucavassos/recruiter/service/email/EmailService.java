package dev.lucavassos.recruiter.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Value("${client.base-url}")
    private String clientBaseUrl;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String name, String resetEndpoint) throws MessagingException
    {
        String htmlContent = "Hi %s, <br/><br/> "
                + "<p>You are receiving this email since you requested to reset your password. "
                + "If it wasn't you, please ignore this email altogether.<br/><br/>"
                + "<p>Click the following link below to reset your password: %s/%s </p><br/>"
                + "<h4>The reset link is valid for 15 minutes.</h4><br/><br/>"
                + "With regards,<br/>Recruiter Platform";
        htmlContent = String.format(htmlContent, name, this.clientBaseUrl, resetEndpoint);

        String subject = "Password reset for %s - The Recruiter Platform";
        subject = String.format(subject, name);


        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);

        log.info("Sending mail...");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending mail: {}", e.getMessage());
            throw e;
        }

        log.info("Mail sent successfully!");
    }
}
