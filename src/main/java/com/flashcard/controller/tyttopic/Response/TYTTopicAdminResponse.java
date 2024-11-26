package com.flashcard.controller.tyttopic.Response;

import com.flashcard.model.TYTTopic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TYTTopicAdminResponse {

    private final Long id;

    private final String subject;

    public TYTTopicAdminResponse(TYTTopic topic) {
        id = topic.getId();
        subject = topic.getSubject();
    }
}
