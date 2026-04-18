package com.flashcard.controller.growingreport.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrowingReportTopicResponse {
    private String topic;
    private Integer questionCount;
    private Integer rightCount;
    private Integer wrongCount;
    private Integer percent;
}
