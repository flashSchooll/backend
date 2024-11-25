package com.flashcard.controller.dailytarget.response;


import com.flashcard.model.DailyTarget;
import lombok.Getter;

@Getter
public class DailyTargetResponse {

    private final Integer target;
    private final Integer made;

    public DailyTargetResponse(DailyTarget dailyTarget) {
        this.target = dailyTarget.getTarget();
        this.made = dailyTarget.getMade();
    }
}
