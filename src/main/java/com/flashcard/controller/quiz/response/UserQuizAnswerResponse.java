package com.flashcard.controller.quiz.response;

import com.flashcard.model.UserQuizAnswer;
import com.flashcard.model.enums.QuizOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserQuizAnswerResponse {
    private final QuizOption correctAnswer;
    private final String question;
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final QuizOption usersAnswer;
    private final String name;
    private final boolean isAnswerCorrect;

    public UserQuizAnswerResponse(UserQuizAnswer userQuizAnswer) {
        this.correctAnswer = userQuizAnswer.getQuiz().getAnswer();
        this.question = userQuizAnswer.getQuiz().getQuestion();
        this.a = userQuizAnswer.getQuiz().getA();
        this.b = userQuizAnswer.getQuiz().getB();
        this.c = userQuizAnswer.getQuiz().getC();
        this.d = userQuizAnswer.getQuiz().getD();
        this.usersAnswer = userQuizAnswer.getAnswer();
        this.name = userQuizAnswer.getQuiz().getName();
        this.isAnswerCorrect = userQuizAnswer.getAnswer().equals(userQuizAnswer.getQuiz().getAnswer());
    }
}
