package com.flashcard.controller.topic.admin.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopicSaveRequest {

    private Long lessonId;

    @Size(min = 0,max = 256)
    @NotBlank
    private String subject;
}
