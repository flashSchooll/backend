package com.flashcard.controller.quiz.response;

import com.flashcard.model.enums.QuizType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizCount {
    private String name;
    private Long count;
    private long topicId;
    private boolean seen;
    private QuizType quizType;

    public QuizCount(String name, Long count, long topicId, boolean seen, QuizType type) {
        this.name = name;
        this.count = count;
        this.topicId = topicId;
        this.seen = seen;
        this.quizType = type;
    }
}
