package com.flashcard.controller.quiz.request;

import com.flashcard.model.enums.QuizOption;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizSaveRequest {

    private QuizOption option;
    @NotNull
    private Long quizId;

}
