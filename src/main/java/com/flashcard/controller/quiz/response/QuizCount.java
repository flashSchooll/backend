package com.flashcard.controller.quiz.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizCount {
    private String name;
    private Long count;
    private long topicId;
    private boolean seen;

    public QuizCount(String name, Long count, long topicId, boolean seen) {
        this.name = name;
        this.count = count;
        this.topicId = topicId;
        this.seen = seen;
    }
}
