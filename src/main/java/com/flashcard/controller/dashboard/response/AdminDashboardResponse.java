package com.flashcard.controller.dashboard.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AdminDashboardResponse {

    public final Long countUser;
    public final Long countLesson;
    public final Long countTopic;
    public final Long countTopicSummary;
    public final Long countFlashcard;
    public final Long countCard;
    public final Long countAverageFifty;
}
