package com.flashcard.controller.topicsummary.response;

import com.flashcard.model.TopicSummary;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopicSummaryResponse {

    private final Long id;
    private final String topic;
    private final String summary;

    public TopicSummaryResponse(TopicSummary topicSummary) {
        id = topicSummary.getId();
        topic = topicSummary.getTopic().getSubject();
        summary = topicSummary.getSummary();
    }
}
