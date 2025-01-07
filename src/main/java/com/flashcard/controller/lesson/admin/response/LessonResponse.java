package com.flashcard.controller.lesson.admin.response;

import com.flashcard.model.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {

    private final Long lessonId;
    private final String name;
    private final String branch;
    private final String yks;
    private final byte[] icon;

    public LessonResponse(Lesson lesson) {
        this.lessonId = lesson.getId();
        this.name = lesson.getYksLesson() != null ? lesson.getYksLesson().label : null;
        this.icon = lesson.getIcon();
        this.branch = lesson.getBranch() != null ? lesson.getBranch().label : null;
        this.yks = lesson.getYks().name();
    }
}
