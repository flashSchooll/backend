package com.flashcard.controller.tyttopicsummary.response;

import com.flashcard.model.TYTTopicSummary;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTTopicSummaryResponse {

    private final Long id;
    private final String topic;
    private final String summary;

    public TYTTopicSummaryResponse(TYTTopicSummary topicSummary) {
        id = topicSummary.getId();
        topic = topicSummary.getTopic().getSubject();
        summary = topicSummary.getSummary();
    }
}
