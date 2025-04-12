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
    public final long star;
    public final long rosette;
    public final long order;
    public final boolean me;
    public final String photoPath;
}
