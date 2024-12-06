package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Branch {
    NUMERICAL("Sayısal"),
    EQUAL_WEIGHT("Eşit Ağırlık"),
    VERBAL("Sözel"),
    FOREIGN_LANGUAGE("Yabancı Dil"),
    ALL_GROUPS("Bütün Gruplar");

    public final String label;
}
