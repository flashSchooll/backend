package com.flashcard.controller.tyttopic.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTTopicSaveRequest {

    private Long lessonId;

    @Size(min = 0,max = 256)
    @NotBlank
    private String subject;
}
