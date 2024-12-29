package com.flashcard.model;

import com.flashcard.model.enums.DifficultyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "MyCard")
@Table(name = "my_card",
        indexes = {
                @Index(name = "idx_my_card_user_card", columnList = "user,card"),
                @Index(name = "idx_my_card_user", columnList = "user")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyCard {// kartlarım bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Card card;

    @Enumerated(value = EnumType.STRING)
    private DifficultyLevel difficultyLevel;
}
