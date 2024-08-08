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
public abstract class EmailService {

    @Value("${client.base-url}")
    protected String clientBaseUrl;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(content, "text/html; charset=utf-8");
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
