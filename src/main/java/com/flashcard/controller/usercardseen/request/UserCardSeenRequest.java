package com.flashcard.controller.usercardseen.request;

import com.flashcard.model.enums.DifficultyLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCardSeenRequest {

    @NotNull
    private Long cardId;

    private Boolean stateOfKnowledge;
}
