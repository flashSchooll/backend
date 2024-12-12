package com.flashcard.controller.dailytarget.response;

import com.flashcard.model.DailyTarget;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyTargetStatisticResponse {

    private final Integer percent;
    private final LocalDate day;

    public DailyTargetStatisticResponse(DailyTarget dailyTarget) {
        this.percent = dailyTarget.getMade();
        this.day = dailyTarget.getDay();
    }
}
