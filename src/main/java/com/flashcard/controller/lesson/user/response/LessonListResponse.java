package com.flashcard.controller.lesson.user.response;

import com.flashcard.model.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonListResponse {
    private final Long id;
    private final String lesson;
    private final String icon;

    public LessonListResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.lesson = lesson.getYksLesson().label;
        this.icon = lesson.getPath();
    }
}
