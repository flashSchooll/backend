package com.flashcard.controller.topic.user.response;

import com.flashcard.model.Topic;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopicUserResponse {
    private final Long id;
    private final Integer cardCount;
    private final String subject;
    private final Integer index;

    public TopicUserResponse(Topic topic, Integer card) {
        this.id = topic.getId();
        this.subject = topic.getSubject();
        this.cardCount = card;
        this.index = topic.getIndex();
    }
}
