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

    private final String topic;
    private final String lesson;
    private final Long topicId;
    private final List<FlashcardResponse> flashcards;
    private final LocalDateTime repeatTime;
    private final byte[] icon;

    public RepeatFlashcardResponse(RepeatFlashcard repeatFlashcard,List<Long> flashcardIds) {
        this.topic = repeatFlashcard.getTopic().getSubject();
        this.lesson = repeatFlashcard.getTopic().getLesson().getYksLesson().label;
        this.topicId = repeatFlashcard.getTopic().getId();
        this.repeatTime = repeatFlashcard.getRepeatTime();
        this.flashcards = repeatFlashcard.getFlashcards().stream().map(flashcard -> new FlashcardResponse(flashcard, flashcardIds)).toList();
        this.icon = repeatFlashcard.getTopic().getLesson().getIcon();
    }
}


