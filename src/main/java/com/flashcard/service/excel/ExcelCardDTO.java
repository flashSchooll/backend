package com.flashcard.service.excel;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Integer cardIndex;
    @NotNull
    private Integer flashcardIndex;
    @NotNull
    private Integer topicIndex;
}
