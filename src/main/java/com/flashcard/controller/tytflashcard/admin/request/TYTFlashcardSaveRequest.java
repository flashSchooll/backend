package com.flashcard.controller.tytflashcard.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTFlashcardSaveRequest {

    @NotNull
    private Long tytTopicId;

    @NotBlank
    @Size(min = 0, max = 256)
    private String cardName;

}
