package com.flashcard.controller.tytlesson.user.response;

import com.flashcard.model.UserCardPercentage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTLessonCardSeenCountResponse {

    private final Long lessonId;
    private final String lesson;
    private final Integer totalCard;
    private final Integer completedCard;

    public TYTLessonCardSeenCountResponse(UserCardPercentage userCardPercentage) {
        this.lessonId=userCardPercentage.getLesson().getId();
        this.lesson = userCardPercentage.getLesson().getTyt().label;
        this.totalCard = userCardPercentage.getTotalCard();
        this.completedCard = userCardPercentage.getCompletedCard();
        ;
    }
}
