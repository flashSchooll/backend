package com.flashcard.controller.repeatflashcard.response;

import com.flashcard.controller.tytflashcard.admin.response.TYTFlashcardResponse;
import com.flashcard.model.RepeatFlashcard;
import com.flashcard.model.TYTFlashcard;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
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
    private List<TYTFlashcardResponse> tytFlashcard;
    private LocalDateTime repeatTime;

    public RepeatFlashcardResponse(RepeatFlashcard repeatFlashcard) {
        this.topic = repeatFlashcard.getTopic();
        this.lesson = repeatFlashcard.getLesson();
        this.topicId = repeatFlashcard.getTopicId();
        this.repeatTime = repeatFlashcard.getRepeatTime();
        this.tytFlashcard = repeatFlashcard.getTytFlashcard().stream().map(TYTFlashcardResponse::new).toList();
    }
}


