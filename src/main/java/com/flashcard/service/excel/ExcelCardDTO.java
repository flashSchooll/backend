package com.flashcard.service.excel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ExcelCardDTO {

    private String subject;//konu
    private String flashcardName;//flashcard ismi
    private String frontFace;//ön yüz
    private String backFace;//arka yüz
    private MultipartFile frontImage;//ön resim
    private MultipartFile backImage;//arka resim
}
