package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.payload.reminder.ReminderRequestDTO;
import alatoo.edu.kg.api.payload.reminder.ReminderResponseDTO;

import java.util.List;

public interface ReminderService {

    ReminderResponseDTO createReminder(Long postId, ReminderRequestDTO dto);
    List<ReminderResponseDTO> getUserReminders();
    void deleteReminder(Long reminderId);
}
