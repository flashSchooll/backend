package com.flashcard.controller.statistic.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCardStatisticResponse {

    private Integer totalCard;
    private Integer seenCard;
    private Integer unseenCard;
    private Double percentage;
}
