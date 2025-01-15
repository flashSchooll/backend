package com.flashcard.controller.quiz.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizCount {
    private String name;
    private Long count;
    private long topicId;

    public QuizCount(String name, Long count, long topicId) {
        this.name = name;
        this.count = count;
        this.topicId = topicId;
    }
}
