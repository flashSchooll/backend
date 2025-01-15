package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum QuizOption {
    A("A"),
    B("B"),
    C("C"),
    D("D");

    private final String label;

    public static QuizOption byLabel(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Şık değeri boş olamaz");
        }

        return Arrays.stream(values())
                .filter(quizOption -> Objects.equals(quizOption.label, value))
                .findAny().orElseThrow();
    }
}
