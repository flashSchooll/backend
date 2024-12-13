package com.flashcard.controller.dashboard.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AdminDashboardResponse {

    public Long countUser;
    public Long countLesson;
    public Long countTopic;
    public Long countTopicSummary;
    public Long countFlashcard;
    public Long countCard;
    public Long countAverageFifty;
}
