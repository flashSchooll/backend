package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DifficultyLevel {
    HARD("Zor"),
    MIDDLE("Orta"),
    EASY("Kolay");

    public final String label;
}
