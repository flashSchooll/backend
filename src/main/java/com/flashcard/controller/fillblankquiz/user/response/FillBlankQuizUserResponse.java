package com.flashcard.controller.fillblankquiz.user.response;

import com.flashcard.model.enums.QuizType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FillBlankQuizUserResponse {
    private String title;
    private Long count;
    private boolean seen;
    private long topicId;
    private QuizType quizType;
}
