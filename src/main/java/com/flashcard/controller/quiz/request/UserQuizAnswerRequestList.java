package com.flashcard.controller.quiz.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserQuizAnswerRequestList {

    @NotBlank
    private String name;
    @NotNull
    private Long topicId;
    @Size(min = 1)
    private List<UserQuizAnswerRequest> answerList;
}
