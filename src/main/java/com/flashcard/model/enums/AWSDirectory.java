package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AWSDirectory {
    CARDS("cards/"),
    LESSONS("lessons/"),
    USERS("users/"),
    PODCAST("podcast/"),
    AVATAR("avatar/");

    public final String path;
}
