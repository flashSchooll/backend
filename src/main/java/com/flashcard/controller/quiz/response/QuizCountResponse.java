package com.flashcard.controller.quiz.response;

import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class QuizCountResponse {
    private List<QuizCount> quizList;
    private List<FillBlankQuizUserResponse> fillBlankQuizUserResponses;
}
