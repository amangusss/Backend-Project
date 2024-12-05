package alatoo.edu.kg.api.controller.reminder;

import alatoo.edu.kg.api.payload.reminder.ReminderRequestDTO;
import alatoo.edu.kg.api.payload.reminder.ReminderResponseDTO;
import alatoo.edu.kg.api.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public final class ReminderController implements ReminderControllerDocumentation {

    private final ReminderService reminderService;

    @Override
    public ResponseEntity<ReminderResponseDTO> createReminder(@PathVariable Long postId, @Valid @RequestBody ReminderRequestDTO dto) {
        ReminderResponseDTO createdReminder = reminderService.createReminder(postId, dto);
        return ResponseEntity.status(201).body(createdReminder);
    }

    @Override
    public ResponseEntity<List<ReminderResponseDTO>> getUserReminders() {
        List<ReminderResponseDTO> reminders = reminderService.getUserReminders();
        return ResponseEntity.ok(reminders);
    }

    @Override
    public ResponseEntity<Void> deleteReminder(@PathVariable Long reminderId) {
        reminderService.deleteReminder(reminderId);
        return ResponseEntity.noContent().build();
    }
}
