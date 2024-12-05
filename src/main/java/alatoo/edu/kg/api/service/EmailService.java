package alatoo.edu.kg.api.service;

import alatoo.edu.kg.store.entity.Reminder;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
    void sendNotification(Reminder reminder);
}
