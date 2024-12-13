package alatoo.edu.kg.api.payload.reminder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReminderResponseDTO {

    Long id;
    LocalDateTime reminderTime;
    boolean sent;
    Long postId;
}