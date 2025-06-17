package com.flashcard.controller.topic.admin.response;

import com.flashcard.model.Topic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicAdminResponse {

    private final Long id;
    private final String subject;
    private final String lesson;
    private final String yksLesson;


    public TopicAdminResponse(Topic topic) {
        this.id = topic.getId();
        this.subject = topic.getSubject();
        this.lesson = topic.getLesson().getYksLesson().label;
        this.yksLesson = topic.getLesson().getYksLesson().label;
    }
}
