package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public enum QuizType {
    RIGHT_WRONG,
    TEST;


    public static QuizType by(String type) {
        return Arrays.stream(values())
                .filter(t -> t.name().equals(type))
                .findAny().orElseThrow(()->new NoSuchElementException("Geçersiz quiz tipi"));
    }
}
