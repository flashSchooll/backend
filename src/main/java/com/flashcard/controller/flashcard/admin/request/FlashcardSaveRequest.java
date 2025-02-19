package com.flashcard.controller.flashcard.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlashcardSaveRequest {

    @NotNull
    private Long topicId;

    @NotBlank
    @Size(max = 256)
    private String cardName;

}
