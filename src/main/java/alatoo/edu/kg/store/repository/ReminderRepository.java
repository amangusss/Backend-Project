package alatoo.edu.kg.store.repository;

import alatoo.edu.kg.store.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderTimeBeforeAndSentFalse(LocalDateTime time);
    List<Reminder> findByUserId(Long userId);
}
