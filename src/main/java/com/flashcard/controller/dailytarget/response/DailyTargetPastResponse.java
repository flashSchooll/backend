package com.flashcard.controller.dailytarget.response;

import com.flashcard.model.MonthDailyTarget;
import lombok.Getter;

import java.time.Month;

@Getter
public class DailyTargetPastResponse {

    private final Month month;
    private final Integer average;

    public DailyTargetPastResponse(MonthDailyTarget monthDailyTarget) {
        this.month = monthDailyTarget.getMonth();
        this.average = monthDailyTarget.getAverage();
    }
}
