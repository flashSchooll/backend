package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardFace {
    FRONT("Ön"),
    BACK("Arka");

    public final String label;
}
