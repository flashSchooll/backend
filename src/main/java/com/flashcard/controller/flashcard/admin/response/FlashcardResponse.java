package com.flashcard.controller.flashcard.admin.response;

import com.flashcard.model.Flashcard;
import lombok.Getter;

import java.util.List;

@Getter
public class FlashcardResponse {

    private final Long id;
    private final String topic;
    private final String cardName;
    private final Boolean seen;
    private final String lesson;
    private final boolean published;

    public FlashcardResponse(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
        this.seen = false;
        this.lesson = flashcard.getTopic().getLesson().getYksLesson().label;
        this.published = flashcard.getCanBePublish();
    }

    public FlashcardResponse(Flashcard flashcard, List<Long> flashcardIds) {
        this.id = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
        this.seen = flashcardIds.contains(flashcard.getId());
        this.lesson = flashcard.getTopic().getLesson().getYksLesson().label;
        this.published = flashcard.getCanBePublish();
    }
}
