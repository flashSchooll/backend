package com.flashcard.controller.lesson.admin.response;

import com.flashcard.model.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {

    private final Long lessonId;
    private final String yksLesson;
    private final String branch;
    private final String yks;
    //  private final byte[] icon;
    private final String path;

    public LessonResponse(Lesson lesson) {
        this.lessonId = lesson.getId();
        this.yksLesson = lesson.getYksLesson() != null ? lesson.getYksLesson().name() : null;
        //  this.icon = lesson.getIcon();
        this.branch = lesson.getBranch() != null ? lesson.getBranch().name() : null;
        this.yks = lesson.getYks().name();
        this.path = lesson.getPath();
    }
}
