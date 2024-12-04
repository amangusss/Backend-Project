//package alatoo.edu.kg.store.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "reminders")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Reminder {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    Long id;
//
//    @Column(name = "reminder_time", nullable = false)
//    LocalDateTime reminderTime;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    Entry entry;
//}
