package com.flashcard.controller.dailytarget.response;

import com.flashcard.model.DailyTarget;
import lombok.Getter;

@Getter
public class DailyTargetAdminResponse {

    private final String userName;
    private final String userSurname;
    private final Integer target;
    private final Integer made;

    public DailyTargetAdminResponse(DailyTarget dailyTarget) {
        this.userName = dailyTarget.getUser().getUserName();
        this.userSurname = dailyTarget.getUser().getUserSurname();
        this.target = dailyTarget.getTarget();
        this.made = dailyTarget.getMade();
    }
}
