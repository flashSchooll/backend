package com.flashcard.controller.quiz.response;

import com.flashcard.model.Quiz;
import com.flashcard.model.enums.QuizOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class QuizResponse {
    private final Long id;
    private final String question;
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final QuizOption answer;
    private final String name;
    private final String topic;

    public QuizResponse(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
        this.a = quiz.getA();
        this.b = quiz.getB();
        this.c = quiz.getC();
        this.d = quiz.getD();
        this.answer = quiz.getAnswer();
        this.name = quiz.getName();
        this.topic = quiz.getTopic().getSubject();
    }
}
