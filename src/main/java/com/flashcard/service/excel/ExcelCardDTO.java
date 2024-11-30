package com.flashcard.service.excel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelCardDTO {

    private String subject;//konu
    private String flashcardName;//flashcard ismi
    private String frontFace;//ön yüz
    private String backFace;//arka yüz
    private Byte[] frontImage;//ön resim
    private Byte[] backImage;//arka resim
}
