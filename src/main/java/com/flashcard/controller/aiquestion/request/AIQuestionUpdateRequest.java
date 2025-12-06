package com.flashcard.controller.aiquestion.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AIQuestionUpdateRequest {
    @NotNull
    private String subject;

    private String question;

    @NotNull
    private String answer;

    @NotNull
    @Size(min = 4, max = 5)
    private List<String> options;

    private String level;

    @NotNull
    private String description;
}
