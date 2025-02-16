package com.flashcard.controller.quiz.response;

import com.flashcard.model.MyQuiz;
import com.flashcard.model.enums.QuizOption;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyQuizResponse {
    private final Long id;
    private final QuizOption option;
    private final QuizResponse quizResponse;

    public MyQuizResponse(MyQuiz myQuiz) {
        id = myQuiz.getQuiz().getId();
        option = myQuiz.getOption();
        quizResponse = new QuizResponse(myQuiz.getQuiz());
    }
}
