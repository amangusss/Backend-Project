package alatoo.edu.kg.store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "verification_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name = "token", nullable = false, unique = true)
    String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "expity_date", nullable = false)
    LocalDateTime expiryDate;
}