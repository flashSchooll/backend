package com.flashcard.controller.quiz.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserQuizAnswerRequestList {

    private String name;
    private List<UserQuizAnswerRequest> answerList;
}
