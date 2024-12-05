package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.service.EmailService;
import alatoo.edu.kg.store.entity.Reminder;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    JavaMailSender mailSender;

    @Override
    @Transactional
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("aman.nazarkulov@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

            log.info("Email sent to '{}'", to);
        } catch (Exception e) {
            log.error("Failed to send email to '{}'", to, e);
        }
    }

    @Override
    @Transactional
    public void sendNotification(Reminder reminder) {
        String toEmail = reminder.getUser().getEmail();
        String subject = "Post Reminder: " + reminder.getPost().getTitle();
        String text = "Hello, " + reminder.getUser().getUsername() + "!\n\n"
                + "We remind you about the post: \"" + reminder.getPost().getTitle() + "\".\n\n"
                + "The content of the post:\n" + reminder.getPost().getDescription() + "\n\n"
                + "Sincerely, \nAman Nazarkulov!.";

        sendEmail(toEmail, subject, text);
    }
}


