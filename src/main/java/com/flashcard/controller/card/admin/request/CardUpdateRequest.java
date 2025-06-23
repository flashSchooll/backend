package com.flashcard.controller.card.admin.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardUpdateRequest {

    @Size(max = 256)
    private String frontFace;

    @Size(max = 256)
    private String backFace;

}
