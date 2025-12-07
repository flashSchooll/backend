package com.flashcard.controller.aiquestion.request;

import com.flashcard.controller.aiquestion.response.AIQuestionResponse;
import com.flashcard.model.AIQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AIQuestionAdminResponse {
    private final Long topicId;
    private final String uuid;
    private final boolean published;
    private final List<AIQuestionAdminResponseDetail> aiQuestions;

    public AIQuestionAdminResponse(AIQuestion aiQuestion, List<AIQuestion> aiQuestions) {
        this.topicId = aiQuestion.getTopic().getId();
        this.uuid = aiQuestion.getUuid();
        this.published = aiQuestion.isPublished();
        this.aiQuestions = aiQuestions.stream()
                .map(AIQuestionAdminResponseDetail::new)
                .toList();

    }
}

