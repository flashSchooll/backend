package com.flashcard.controller.repeatflashcard.response;

import com.flashcard.controller.flashcard.admin.response.FlashcardResponse;
import com.flashcard.model.RepeatFlashcard;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class RepeatFlashcardResponse {

    private final Long id;
    private final String topic;
    private final String lesson;
    private final Long topicId;
    private final LocalDateTime repeatTime;
    private final String icon;
    private final String flashcard;

    public RepeatFlashcardResponse(RepeatFlashcard repeatFlashcard, List<Long> flashcardIds) {
        this.id = repeatFlashcard.getId();
        this.topic = repeatFlashcard.getFlashcard().getTopic().getSubject();
        this.lesson = repeatFlashcard.getFlashcard().getTopic().getLesson().getYksLesson().label;
        this.topicId = repeatFlashcard.getFlashcard().getTopic().getId();
        this.repeatTime = repeatFlashcard.getRepeatTime();
        this.icon = repeatFlashcard.getFlashcard().getTopic().getLesson().getPath();
        this.flashcard = repeatFlashcard.getFlashcard().getCardName();
    }
}


