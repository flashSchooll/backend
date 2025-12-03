package com.flashcard.controller.flashcard.user.response;

import com.flashcard.model.Flashcard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlashcardSearchResponse {

    private final String lesson;
    private final Long flashcardId;
    private final String topic;
    private final Long topicId;
    private final String flashcardName;
    private final boolean seen;

    public FlashcardSearchResponse(Flashcard flashcard,boolean seen) {
        this.lesson = flashcard.getTopic().getLesson().getYksLesson().label;
        this.flashcardId = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.topicId = flashcard.getTopic().getId();
        this.flashcardName = flashcard.getCardName();
        this.seen = seen;
    }
}
