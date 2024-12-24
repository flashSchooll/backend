package com.flashcard.controller.lesson.user.response;

import com.flashcard.model.UserCardPercentage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TYTLessonCardSeenCountResponse {

    private final Long lessonId;
    private final String lesson;
    private final Integer totalCard;
    private final Integer completedCard;
    private final byte[] icon;

    public TYTLessonCardSeenCountResponse(UserCardPercentage userCardPercentage) {
        this.lessonId = userCardPercentage.getLesson().getId();
        this.lesson = userCardPercentage.getLesson().getYksLesson().label;
        this.totalCard = userCardPercentage.getTotalCard();
        this.completedCard = userCardPercentage.getCompletedCard();
        this.icon = userCardPercentage.getLesson().getIcon();
    }

    public TYTLessonCardSeenCountResponse(Long lessonId, String lesson, Integer totalCard, Integer completedCard, byte[] icon) {
        this.lessonId = lessonId;
        this.lesson = lesson;
        this.totalCard = totalCard;
        this.completedCard = completedCard;
        this.icon = icon;
    }
}
