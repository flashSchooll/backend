package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum DifficultyLevel {
    HARD("Zor", 1),
    MIDDLE("Orta", 2),
    EASY("Kolay", 3);

    private final String label;
    private final int value; // 'boolean' yerine 'int' yapıldı

    public static Optional<DifficultyLevel> by(String level) {
        if (level == null) return Optional.empty(); // Null check eklendi
        return Arrays.stream(values())
                .filter(difficultyLevel -> difficultyLevel.name().equalsIgnoreCase(level)) // Büyük/küçük harf duyarlılığını kaldırmak daha güvenlidir
                .findAny();
    }

    public static DifficultyLevel byValue(Integer level) {
        return Arrays.stream(values())
                .filter(difficultyLevel -> difficultyLevel.getValue() == level) // Karşılaştırma mantığı düzeltildi
                .findAny().orElseThrow(()-> new IllegalArgumentException("yanlış zorluk değeri"));
    }
}
