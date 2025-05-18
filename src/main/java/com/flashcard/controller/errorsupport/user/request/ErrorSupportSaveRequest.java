package com.flashcard.controller.errorsupport.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ErrorSupportSaveRequest(@NotBlank @Size(max = 255) String errorMessage,
                                      @NotNull Long cardId) {
}

