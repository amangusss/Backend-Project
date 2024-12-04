package alatoo.edu.kg.api.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
