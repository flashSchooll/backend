package com.flashcard.controller.aiquestion.response;

import com.flashcard.model.AIQuestion;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AIQuestionResponse {
    private final String id;
    private final Long topicId;
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
    private final boolean published;
    private final String uuid;

    public AIQuestionResponse(AIQuestion aiQuestion) {
        this.id = aiQuestion.getId();
        this.topicId = aiQuestion.getTopic().getId();
        this.subject = aiQuestion.getSubject();
        this.question = aiQuestion.getQuestion();
        this.answer = aiQuestion.getAnswer();
        this.a = aiQuestion.getA();
        this.b = aiQuestion.getB();
        this.c = aiQuestion.getC();
        this.d = aiQuestion.getD();
        this.e = aiQuestion.getE();
        this.level = aiQuestion.getLevel();
        this.description = aiQuestion.getDescription();
        this.published = aiQuestion.isPublished();
        this.uuid = aiQuestion.getUuid();
    }
}
