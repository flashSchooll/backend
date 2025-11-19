package com.flashcard.controller.repeattopic.response;

import com.flashcard.model.RepeatTopic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RepeatTopicResponse {
    private final Long topicId;
    private final LocalDateTime repeatTime;
    private final String topic;

    public RepeatTopicResponse(RepeatTopic repeatTopic) {
        this.topicId = repeatTopic.getTopic().getId();
        this.repeatTime = repeatTopic.getRepeatTime();
        this.topic = repeatTopic.getTopic().getSubject();
    }
}
