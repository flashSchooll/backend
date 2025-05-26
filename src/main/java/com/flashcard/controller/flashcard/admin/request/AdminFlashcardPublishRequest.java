package com.flashcard.controller.flashcard.admin.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminFlashcardPublishRequest {
    @Size(min = 1)
    private List<Long> flashcardIds;
}
