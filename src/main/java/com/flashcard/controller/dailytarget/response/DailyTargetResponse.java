package com.flashcard.controller.dailytarget.response;


import com.flashcard.model.DailyTarget;
import lombok.Getter;

@Getter
public class DailyTargetResponse {

    private final Integer target;
    private final Integer made;
    private final Integer targetSeries;

    public DailyTargetResponse(DailyTarget dailyTarget) {
        this.target = dailyTarget.getUser().getTarget();
        this.made = dailyTarget.getMade();
        this.targetSeries = dailyTarget.getUser().getTargetSeries();
    }
}
