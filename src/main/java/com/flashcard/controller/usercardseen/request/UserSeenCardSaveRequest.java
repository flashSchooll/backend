package com.flashcard.controller.usercardseen.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserSeenCardSaveRequest {

    @NotNull
    private Long flashcardId;

    @Valid
    private List<UserSeenCardRequest> userCardSeenRequestList;

    @NotNull
    private Long minute;

    @NotNull
    private Long second;
}
