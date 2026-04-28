package com.flashcard.controller.growingreport.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrowingReportTopicResponse {
    private String topic;

    // TEST istatistikleri
    private Integer testQuestionCount;
    private Integer testRightCount;
    private Integer testWrongCount;
    private Integer testPercent;

    // RIGHT_WRONG istatistikleri
    private Integer rightWrongQuestionCount;
    private Integer rightWrongRightCount;
    private Integer rightWrongWrongCount;
    private Integer rightWrongPercent;

    // Genel toplam (opsiyonel, istersen kaldırabilirsin)
    private Integer totalQuestionCount;
    private Integer totalRightCount;
    private Integer totalWrongCount;
    private Integer totalPercent;
}
