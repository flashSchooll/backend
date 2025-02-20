package com.flashcard.controller.ownerflashcard.user.response;

import com.flashcard.model.OwnerFlashcard;
import com.flashcard.model.enums.YKSLesson;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OwnerFlashcardResponse {

    private final long id;
    private final YKSLesson yksLesson;
    private final String name;

    public OwnerFlashcardResponse(OwnerFlashcard ownerFlashcard) {

        this.id = ownerFlashcard.getId();
        this.yksLesson = ownerFlashcard.getLesson();
        this.name = ownerFlashcard.getName();

    }
}
