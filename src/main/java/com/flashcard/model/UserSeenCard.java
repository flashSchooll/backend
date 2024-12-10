package com.flashcard.model;


import com.flashcard.model.enums.DifficultyLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity(name = "UserSeenCard")
@Table(name = "user_seen_card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSeenCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Card card;

    private Boolean stateOfKnowledge;

    private Duration duration;
}
