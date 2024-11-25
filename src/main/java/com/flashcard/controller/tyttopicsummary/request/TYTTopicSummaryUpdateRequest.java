package com.flashcard.controller.tyttopicsummary.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTTopicSummaryUpdateRequest {

    @NotNull
    private Long summary_id;

    @NotBlank
    @Size(min = 0,max = 1024)
    private String summary;
}
