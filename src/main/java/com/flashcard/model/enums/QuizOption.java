package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum QuizOption {
    A("A",0),
    B("B",1),
    C("C",2),
    D("D",3),
    E("E",4);

    private final String label;
    private final int index;

    public static QuizOption byLabel(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Şık değeri boş olamaz");
        }

        return Arrays.stream(values())
                .filter(quizOption -> Objects.equals(quizOption.label, value))
                .findAny().orElseThrow();
    }
    public static QuizOption byIndex(Integer index) {
        if (index == null) {
            throw new IllegalArgumentException("Şık değeri boş olamaz");
        }

        return Arrays.stream(values())
                .filter(quizOption -> Objects.equals(quizOption.index, index))
                .findAny().orElseThrow();
    }
}
