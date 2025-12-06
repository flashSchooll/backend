package com.flashcard.controller.statistic.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserRosetteStatistic {
    public final Long id;
    public final String userName;
    public final String userSurname;
    public final Integer star;
    public final Integer weeklyStar;
    public final Integer rosette;
    public final Integer order;
    public final Boolean me;
    public final String photoPath;
}
