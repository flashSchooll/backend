package com.flashcard.controller.tytlessonadmin.response;

import com.flashcard.model.TYTLesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TYTLessonResponse {

    private final Long id;
    private final String name;

    public TYTLessonResponse(TYTLesson tytLesson) {
        this.id = tytLesson.getId();
        this.name = tytLesson.getTyt().label;
    }
}
