package com.flashcard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password_reset_code")
@Entity(name = "PasswordResetCode")
@SQLDelete(sql = "UPDATE password_reset_code SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(nullable = false,
            name = "user_id")
    private User user;

    private boolean deleted = false;

    public PasswordResetCode(String code, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}