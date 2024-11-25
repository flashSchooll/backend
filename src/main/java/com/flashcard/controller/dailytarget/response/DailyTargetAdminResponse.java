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
        this.userName = this.getUserName();
        this.userSurname = this.getUserSurname();
        this.target = this.getTarget();
        this.made = this.target;
    }
}
