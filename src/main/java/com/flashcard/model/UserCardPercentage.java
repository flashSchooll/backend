package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "UserCardPercentage")
@Table(name = "user_card_percentage",
        indexes = {
                @Index(name = "idx_user_card_percentage_user", columnList = "user"),
                @Index(name = "idx_user_card_percentage_lesson", columnList = "lesson"),
                @Index(name = "idx_user_card_percentage_user_lesson", columnList = "user,lesson")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "user_card_user_lesson", columnNames = {"user", "lesson"})
        })
@SQLDelete(sql = "UPDATE user_card_percentage SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class UserCardPercentage {// kullanıcı kart tamamlama yüzdesini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Lesson lesson;//todo entity graph eklenecek çok fazla sorgu atılıyor

    private Set<Long> flashCards = new HashSet<>();

    private Integer totalCard;

    private Integer completedCard;

    private boolean deleted = false;

    public void increaseCompletedCard(Integer amount) {
        this.completedCard = getCompletedCard() + amount;
    }
}
