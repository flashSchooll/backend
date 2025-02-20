package com.flashcard.controller.ownercard.response;

import com.flashcard.service.OwnerCard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OwnerCardResponse {

    private final String frontFace;

    private final String backFace;

    public OwnerCardResponse(OwnerCard ownerCard) {
        this.frontFace = ownerCard.getFrontFace();
        this.backFace = ownerCard.getBackFace();
    }
}
