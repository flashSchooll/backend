package com.flashcard.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TYT {
    TEMEL_MATEMATIK("TEMEL Matematik"),
    TEMEL_TURKCE("TEMEL Türkçe"),
    TYT_FIZIK("TYT Fizik"),
    TYT_KIMYA("TYT Kimya"),
    TYT_BIYOLOJI("TYT Biyoloji"),
    TYT_DIN_KULTURU_VE_AHLAK_BILGISI("TYT Din Kültürü ve Ahlak Bilgisi"),
    TYT_FELSEFE_GRUBU("TYT Felsefe Grubu"),
    TYT_TARIH("TYT Tarih");

    public final String label;

}
