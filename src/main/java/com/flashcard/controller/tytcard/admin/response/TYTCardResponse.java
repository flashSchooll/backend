package com.flashcard.controller.tytcard.admin.response;

import com.flashcard.model.TYTCard;
import lombok.Getter;

@Getter
public class TYTCardResponse {

    private final Long id;

    private final Long tytFlashcardId;

    private final String frontFace;

    private final String backFace;

    private final byte[] dataFrontFace;

    private final byte[] dataBackFace;

    public TYTCardResponse(TYTCard tytCard) {
        this.id = tytCard.getId();
        this.tytFlashcardId = tytCard.getTytFlashcard().getId();
        this.frontFace = tytCard.getFrontFace();
        this.backFace = tytCard.getBackFace();
        this.dataFrontFace = tytCard.getDataFrontFace();
        this.dataBackFace = tytCard.getDataBackFace();
    }
}
