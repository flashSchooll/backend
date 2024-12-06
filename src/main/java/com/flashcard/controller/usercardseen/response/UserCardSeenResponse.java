package com.flashcard.controller.usercardseen.response;

import com.flashcard.model.UserSeenCard;
import com.flashcard.model.enums.DifficultyLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Getter
public class UserCardSeenResponse {

    private final String frontFace;
    private final Boolean stateOfKnowledge;
    private final DifficultyLevel difficultyLevel;
    private final Duration duration;

    public UserCardSeenResponse(UserSeenCard seenCard) {
        this.frontFace = seenCard.getCard().getFrontFace();
        this.stateOfKnowledge = seenCard.getStateOfKnowledge();
        this.difficultyLevel = seenCard.getDifficultyLevel();
        this.duration = seenCard.getDuration();
    }
}
