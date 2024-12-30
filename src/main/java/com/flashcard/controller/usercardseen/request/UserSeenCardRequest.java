package com.flashcard.controller.usercardseen.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSeenCardRequest {

    @NotNull
    private Long cardId;

    private Boolean stateOfKnowledge;
}
