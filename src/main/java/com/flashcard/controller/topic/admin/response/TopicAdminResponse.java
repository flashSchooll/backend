package com.flashcard.controller.topic.admin.response;

import com.flashcard.model.Topic;
import com.flashcard.model.enums.YKS;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicAdminResponse {

    private final Long id;
    private final String subject;
    private final String lesson;
    private final String yksLesson;
    private final YKS yks;


    public TopicAdminResponse(Topic topic) {
        this.id = topic.getId();
        this.subject = topic.getSubject();
        this.lesson = topic.getLesson().getYksLesson().label;
        this.yksLesson = topic.getLesson().getYksLesson().label;
        this.yks = topic.getLesson().getYks();
    }
}
