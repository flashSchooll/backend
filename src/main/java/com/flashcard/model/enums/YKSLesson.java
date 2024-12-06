package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YKSLesson {
    MATEMATIK("Matematik"),
    TURKCE("Türkçe"),
    FIZIK("Fizik"),
    KIMYA("Kimya"),
    BIYOLOJI("Biyoloji"),
    DIN_KULTURU_VE_AHLAK_BILGISI("Din Kültürü ve Ahlak Bilgisi"),
    FELSEFE("Felsefe"),
    TARIH("Tarih"),
    COGRAFYA("Coğrafya"),
    EDEBIYAT("Edebiyat"),
    TARIH_1("Tarih-1"),
    COGRAFYA_1("Coğrafya-1"),
    TARIH_2("Tarih-2"),
    COGRAFYA_2("Coğrafya-2"),
    YABANCI_DIL("Yabancı Dil");

    public final String label;

}
