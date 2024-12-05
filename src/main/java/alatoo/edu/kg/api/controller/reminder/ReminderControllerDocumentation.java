package alatoo.edu.kg.api.controller.reminder;

import alatoo.edu.kg.api.payload.reminder.ReminderRequestDTO;
import alatoo.edu.kg.api.payload.reminder.ReminderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/posts")
@Tag(name = "Reminders", description = "Endpoints for managing reminders")
public sealed interface ReminderControllerDocumentation permits ReminderController {

    @Operation(summary = "Create a new reminder", description = "Creates a new reminder for a post.")
    @PostMapping("/{postId}/reminders")
    ResponseEntity<ReminderResponseDTO> createReminder(
            @Parameter(description = "ID of the post", required = true) @PathVariable Long postId,
            @Valid @RequestBody ReminderRequestDTO dto);

    @Operation(summary = "Get user reminders", description = "Fetches all reminders of the current user.")
    @GetMapping("/reminders")
    ResponseEntity<List<ReminderResponseDTO>> getUserReminders();

    @Operation(summary = "Delete a reminder", description = "Deletes a reminder by its ID.")
    @DeleteMapping("/reminders/{reminderId}")
    ResponseEntity<Void> deleteReminder(
            @Parameter(description = "ID of the reminder to be deleted", required = true)
            @PathVariable Long reminderId);
}
