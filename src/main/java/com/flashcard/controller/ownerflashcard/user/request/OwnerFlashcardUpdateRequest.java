package com.flashcard.controller.ownerflashcard.user.request;

import com.flashcard.model.enums.YKSLesson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OwnerFlashcardUpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private YKSLesson yksLesson;
}
