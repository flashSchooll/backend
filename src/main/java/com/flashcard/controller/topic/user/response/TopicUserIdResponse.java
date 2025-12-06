package com.flashcard.controller.topic.user.response;

import com.flashcard.model.Topic;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class TopicUserIdResponse {
    private final Long id;
    private final String subject;

    public TopicUserIdResponse(Topic topic) {
        this.id = topic.getId();
        this.subject = topic.getSubject();
    }
}
