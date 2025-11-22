package com.flashcard.controller.dailytarget.response;

import com.flashcard.model.DailyTarget;
import com.flashcard.model.enums.YKS;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
public class DailyTargetStatisticResponse {

    private final Integer percent;
    private final LocalDate day;
    private final DayOfWeek dayOfWeek;

    public DailyTargetStatisticResponse(DailyTarget dailyTarget) {
        this.percent = dailyTarget.getMade();
        this.day = dailyTarget.getDay();
        this.dayOfWeek = dailyTarget.getDay().getDayOfWeek();
    }

    public DailyTargetStatisticResponse(DailyTarget dailyTarget, YKS yks) {
        if (yks == YKS.AYT) {
            this.percent = dailyTarget.getMadeAyt();
        } else {
            this.percent = dailyTarget.getMadeTyt();
        }
        this.day = dailyTarget.getDay();
        this.dayOfWeek = dailyTarget.getDay().getDayOfWeek();

    }
}
