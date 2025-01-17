package com.flashcard.controller.quiz.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserQuizAnswerRequest {
    @NotNull
    private Long quizId;
    @Max(3)
    @Min(0)
    @NotNull
    private Integer index;
}
