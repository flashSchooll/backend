package com.flashcard.controller.aiquestionpool.response;

import com.flashcard.model.AIQuestionUser;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;

@Getter
@Setter
public class AIQuestionUserResponse {
    private final String id;
    private final String topic;
    private final String subject;
    private final String question;
    private final String answer;
    private final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String level;
    private final String description;
    private final Duration timeSpent;

    public AIQuestionUserResponse(AIQuestionUser ai) {
        this.id = ai.getAiQuestion().getId();
        this.topic = ai.getAiQuestion().getTopic().getSubject();
        this.subject = ai.getAiQuestion().getSubject();
        this.question = ai.getAiQuestion().getQuestion();
        this.answer = ai.getAiQuestion().getAnswer();
        this.a = ai.getAiQuestion().getA();
        this.b = ai.getAiQuestion().getB();
        this.c = ai.getAiQuestion().getC();
        this.d = ai.getAiQuestion().getD();
        this.e = ai.getAiQuestion().getE();
        this.level = ai.getAiQuestion().getLevel();
        this.description = ai.getAiQuestion().getDescription();
        this.timeSpent = ai.getTimeSpent();
    }
}
