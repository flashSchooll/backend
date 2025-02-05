package com.flashcard.controller.quiz.response;

import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import com.flashcard.model.enums.QuizType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class QuizCountResponse {
    private final String name;
    private final Long count;
    private final long topicId;
    private final boolean seen;
    private final QuizType quizType;

    public QuizCountResponse(QuizCount quizCount) {
        this.name = quizCount.getName();
        this.count = quizCount.getCount();
        this.topicId = quizCount.getTopicId();
        this.seen = quizCount.isSeen();
        this.quizType = quizCount.getQuizType();
    }

    public QuizCountResponse(FillBlankQuizUserResponse quizUserResponse) {
        this.name = quizUserResponse.getTitle();
        this.count = quizUserResponse.getCount();
        this.topicId = quizUserResponse.getTopicId();
        this.seen = quizUserResponse.isSeen();
        this.quizType = quizUserResponse.getQuizType();
    }
}
