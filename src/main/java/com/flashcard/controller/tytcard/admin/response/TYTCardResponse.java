package com.flashcard.controller.tytcard.admin.response;

import com.flashcard.model.ImageData;
import com.flashcard.model.TYTCard;
import com.flashcard.model.enums.CardFace;
import lombok.Getter;

import java.util.List;

@Getter
public class TYTCardResponse {

    private final Long id;

    private final Long tytFlashcardId;

    private final String frontFace;

    private final String backFace;

    private final byte[] dataFrontFace;

    private final byte[] dataBackFace;

    public TYTCardResponse(TYTCard tytCard) {

        List<ImageData> imageData = tytCard.getImageData();

        byte[] frontImage = null;
        byte[] backImage = null;

        for (ImageData data : imageData) {
            if (data.getFace().equals(CardFace.FRONT)) {
                frontImage = data.getData();
            } else {
                backImage = data.getData();
            }
        }

        this.id = tytCard.getId();
        this.tytFlashcardId = tytCard.getTytFlashcard().getId();
        this.frontFace = tytCard.getFrontFace();
        this.backFace = tytCard.getBackFace();
        this.dataFrontFace = frontImage;
        this.dataBackFace = backImage;
    }
}
