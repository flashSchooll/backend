package com.flashcard.controller.lesson.user.response;

import com.flashcard.model.Lesson;
import com.flashcard.model.enums.YKSLesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonListResponse {
    private final Long id;
    private final String lesson;
    private final String icon;
    private final String yks;
    private final YKSLesson yksLesson;

    public LessonListResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.lesson = lesson.getYksLesson().label;
        this.icon = lesson.getPath();
        this.yks = lesson.getYks().name();
        this.yksLesson = lesson.getYksLesson();
    }
}
