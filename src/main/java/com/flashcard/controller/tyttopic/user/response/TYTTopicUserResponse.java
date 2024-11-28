package com.flashcard.controller.tyttopic.user.response;

import com.flashcard.model.TYTTopic;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTTopicUserResponse {
    private final Long id;
    private final Integer cardCount;
    private final String subject;

    public TYTTopicUserResponse(TYTTopic topic, Integer card) {
        this.id = topic.getId();
        this.subject = topic.getSubject();
        this.cardCount = card;
    }
}
