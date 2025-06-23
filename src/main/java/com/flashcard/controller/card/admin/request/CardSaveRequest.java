package com.flashcard.controller.card.admin.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardSaveRequest {

    private Long tytFlashcardId;

    @Size(max = 512)
    private String frontFace;

    @Size(max = 512)
    private String backFace;

}
