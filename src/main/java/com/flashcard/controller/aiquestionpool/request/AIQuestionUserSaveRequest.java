package com.flashcard.controller.aiquestionpool.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Getter
public class AIQuestionUserSaveRequest {
    @NotNull(message = "AIQuestion  boş olamaz")
    private String aiQuestionId;

    @NotBlank(message = "Kullanıcı cevabı belirtilmelidir")
    private String userAnswer;

    @NotNull
    private Duration timeSpent;
}
