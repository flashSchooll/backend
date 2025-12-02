package com.flashcard.controller.ownercard.response;

import com.flashcard.model.OwnerCard;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class OwnerCardResponse {

    private final Long id;

    private final String frontFace;

    private final String backFace;

    private final LocalDateTime createdDate;

    public OwnerCardResponse(OwnerCard ownerCard) {
        this.id = ownerCard.getId();
        this.frontFace = ownerCard.getFrontFace();
        this.backFace = ownerCard.getBackFace();
        this.createdDate = ownerCard.getCreatedDate();
    }
}
