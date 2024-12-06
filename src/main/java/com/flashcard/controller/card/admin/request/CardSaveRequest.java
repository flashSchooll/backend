package com.flashcard.controller.card.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class CardSaveRequest {

    private Long tytFlashcardId;

    @Size(min = 0, max = 512)
    @NotBlank
    private String frontFace;

    @Size(min = 0, max = 512)
    @NotBlank
    private String backFace;

    private MultipartFile frontFile;

    private MultipartFile backFile;


}
