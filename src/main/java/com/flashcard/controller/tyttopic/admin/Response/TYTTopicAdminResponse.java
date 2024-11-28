package com.flashcard.controller.tyttopic.admin.Response;

import com.flashcard.model.TYTTopic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TYTTopicAdminResponse {

    private final Long id;
    private final String subject;


    public TYTTopicAdminResponse(TYTTopic topic) {
        this.id = topic.getId();
        this.subject = topic.getSubject();
    }
}
