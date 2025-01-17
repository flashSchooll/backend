package com.flashcard.controller.quiz.response;

import com.flashcard.model.Quiz;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class QuizResponse {
    private final Long id;
    private final String question;
    private final List<String> optionList;
    private final String a;//todo kaldırılacak
    private final String b;
    private final String c;
    private final String d;
    private final Integer answer;
    private final String name;
    private final String topic;

    public QuizResponse(Quiz quiz) {
        this.id = quiz.getId();
        this.question = quiz.getQuestion();
        this.a = quiz.getA();
        this.b = quiz.getB();
        this.c = quiz.getC();
        this.d = quiz.getD();
        this.optionList = List.of(quiz.getA(), quiz.getB(), quiz.getC(), quiz.getD());
        this.answer = quiz.getAnswer().getIndex();
        this.name = quiz.getName();
        this.topic = quiz.getTopic().getSubject();
    }
}
