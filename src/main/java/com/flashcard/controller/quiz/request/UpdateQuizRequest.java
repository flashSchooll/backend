package com.flashcard.controller.quiz.request;

import com.flashcard.model.enums.QuizOption;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateQuizRequest {
    @NotNull
    private Long id;
    private String question;
    private String a;
    private String b;
    private String c;
    private String d;
    private QuizOption answer;
    private String name;
}
