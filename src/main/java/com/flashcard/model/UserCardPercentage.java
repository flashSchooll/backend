package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "UserCardPercentage")
@Table(name = "user_card_percentage")
public class UserCardPercentage {// kullanıcı kart tamamlama yüzdesini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Lesson lesson;

    private List<Long> flashCards;

    private Integer totalCard;

    private Integer completedCard;

    public void increaseCompletedCard(Integer amount) {
        this.completedCard = getCompletedCard() + amount;
    }
}
