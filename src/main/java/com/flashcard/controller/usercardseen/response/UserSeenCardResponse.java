package com.flashcard.controller.usercardseen.response;

import com.flashcard.model.UserSeenCard;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Getter
public class UserSeenCardResponse {

    private final String frontFace;
    private final String backFace;
    private final Boolean stateOfKnowledge;
    private final Duration duration;

    public UserSeenCardResponse(UserSeenCard seenCard) {
        this.frontFace = seenCard.getCard().getFrontFace();
        this.backFace = seenCard.getCard().getBackFace();
        this.stateOfKnowledge = seenCard.getStateOfKnowledge();
        this.duration = seenCard.getDuration();
    }
}
