package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.service.EmailService;
import alatoo.edu.kg.store.entity.Reminder;
import alatoo.edu.kg.store.repository.ReminderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReminderScheduler {

    ReminderRepository reminderRepository;
    EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderRepository.findByReminderTimeBeforeAndSentFalse(now);

        for (Reminder reminder : reminders) {
            emailService.sendNotification(reminder);

            reminder.setSent(true);
            reminderRepository.save(reminder);
        }
    }
}
