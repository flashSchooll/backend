package com.flashcard.controller.tyttopic.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTTopicUpdateRequest {

    @NotNull
    private Long topicId;

    @Size(min = 0,max = 256)
    @NotBlank
    private String subject;
}
