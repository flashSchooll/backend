package com.flashcard.controller.statistic.response;

import com.flashcard.controller.dailytarget.response.DailyTargetStatisticResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserStatisticAllResponse {
    private List<DailyTargetStatisticResponse> dailyTargetStatisticResponse;
    private UserCardStatisticResponse userCardStatisticResponse;
    private List<UserStatisticLessonResponse> userStatisticLessonResponses;
}
