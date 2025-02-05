package com.flashcard.controller.fillblankquiz.user.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FillBlankQuizUserResponse {
    private String title;
    private Long count;
    private boolean seen;
    private long topicId;
}
