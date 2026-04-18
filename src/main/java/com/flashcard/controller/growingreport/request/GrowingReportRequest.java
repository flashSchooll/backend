package com.flashcard.controller.growingreport.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class GrowingReportRequest {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
