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
    private List<FlashcardResponse> tytFlashcard;
    private LocalDateTime repeatTime;

    public RepeatFlashcardResponse(RepeatFlashcard repeatFlashcard) {
        this.topic = repeatFlashcard.getTopic();
        this.lesson = repeatFlashcard.getLesson();
        this.topicId = repeatFlashcard.getTopicId();
        this.repeatTime = repeatFlashcard.getRepeatTime();
        this.tytFlashcard = repeatFlashcard.getFlashcards().stream().map(FlashcardResponse::new).toList();
    }
}


