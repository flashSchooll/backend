package com.flashcard.controller.tytlesson.admin.response;

import com.flashcard.model.TYTLesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TYTLessonResponse {

    private final Long lessonId;
    private final String name;

    public TYTLessonResponse(TYTLesson tytLesson) {
        this.lessonId = tytLesson.getId();
        this.name = tytLesson.getTyt().label;
    }
}
