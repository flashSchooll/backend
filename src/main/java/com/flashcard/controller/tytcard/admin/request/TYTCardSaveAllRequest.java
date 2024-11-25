package com.flashcard.controller.tytcard.admin.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TYTCardSaveAllRequest {
    @Size(max = 20)
    @Valid
    List<TYTCardSaveRequest> tytCardSaveRequests;
}
