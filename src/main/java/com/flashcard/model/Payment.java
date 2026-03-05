package com.flashcard.model;

import com.flashcard.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Payment")
@Table(name = "payments")
@SQLDelete(sql = "UPDATE payments SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount; // Ödeme tutarı

    private String transactionId; // Ödeme sağlayıcısından dönen kod

    private LocalDateTime paymentDate;

    private LocalDateTime expireDate; // Abonelik bitiş tarihi

    private PaymentType paymentType;

    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}