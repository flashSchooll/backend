package com.flashcard.controller.repeattopic.response;

import com.flashcard.model.RepeatTopic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RepeatTopicResponse {
    private final Long topicId;
    private final LocalDateTime repeatTime;
    private final String topic;
    private final String lesson;
    private final Integer cardCount;
    private final Integer index;
   // private final Integer cardCount;  topicdeki değerlerin aynısı dönülecek

    public RepeatTopicResponse(RepeatTopic repeatTopic) {
        this.topicId = repeatTopic.getTopic().getId();
        this.repeatTime = repeatTopic.getRepeatTime();
        this.topic = repeatTopic.getTopic().getSubject();
        this.lesson = repeatTopic.getTopic().getLesson().getYksLesson().label;
        this.index = repeatTopic.getTopic().getIndex();
        this.cardCount = repeatTopic.getTopic().getCardCount();
    }
}
