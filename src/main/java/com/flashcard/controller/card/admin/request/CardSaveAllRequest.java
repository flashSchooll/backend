package com.flashcard.controller.card.admin.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardSaveAllRequest {
    @Size(max = 20)
    @Valid
    List<CardSaveRequest> cardSaveRequests;
}
