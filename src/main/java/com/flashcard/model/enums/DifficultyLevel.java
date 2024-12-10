package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum DifficultyLevel {
    HARD("Zor"),
    MIDDLE("Orta"),
    EASY("Kolay");

    public final String label;

    public static Optional<DifficultyLevel> by(String level) {
        return Arrays.stream(values())
                .filter(difficultyLevel -> Objects.equals(difficultyLevel.name(), level))
                .findAny();
    }
}
