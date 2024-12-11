package com.flashcard.controller.card.admin.response;

import com.flashcard.model.ImageData;
import com.flashcard.model.Card;
import com.flashcard.model.MyCard;
import com.flashcard.model.enums.CardFace;
import com.flashcard.model.enums.DifficultyLevel;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class CardResponse {

    private final Long id;

    private final Long tytFlashcardId;

    private final String frontFace;

    private final String backFace;

    private final byte[] dataFrontFace;

    private final byte[] dataBackFace;

    private final DifficultyLevel difficultyLevel;

    private final boolean isSaved;

    public CardResponse(Card card) {

        List<ImageData> imageData = card.getImageData();

        byte[] frontImage = null;
        byte[] backImage = null;

        for (ImageData data : imageData) {
            if (data.getFace().equals(CardFace.FRONT)) {
                frontImage = data.getData();
            } else {
                backImage = data.getData();
            }
        }

        this.id = card.getId();
        this.tytFlashcardId = card.getFlashcard().getId();
        this.frontFace = card.getFrontFace();
        this.backFace = card.getBackFace();
        this.dataFrontFace = frontImage;
        this.dataBackFace = backImage;
        this.difficultyLevel = null;
        this.isSaved = false;
    }

    public CardResponse(Card card, List<MyCard> myCards) {

        List<ImageData> imageData = card.getImageData();

        byte[] frontImage = null;
        byte[] backImage = null;

        for (ImageData data : imageData) {
            if (data.getFace().equals(CardFace.FRONT)) {
                frontImage = data.getData();
            } else {
                backImage = data.getData();
            }
        }

        MyCard myCard = myCards.stream()
                .filter(cards -> Objects.equals(cards.getCard().getId(), card.getId()))
                .findAny().orElse(null);

        this.id = card.getId();
        this.tytFlashcardId = card.getFlashcard().getId();
        this.frontFace = card.getFrontFace();
        this.backFace = card.getBackFace();
        this.dataFrontFace = frontImage;
        this.dataBackFace = backImage;
        this.difficultyLevel = myCard != null ? myCard.getDifficultyLevel() : null;
        this.isSaved = myCard != null;

    }

    public CardResponse(MyCard myCard) {

        List<ImageData> imageData = myCard.getCard().getImageData();

        byte[] frontImage = null;
        byte[] backImage = null;

        for (ImageData data : imageData) {
            if (data.getFace().equals(CardFace.FRONT)) {
                frontImage = data.getData();
            } else {
                backImage = data.getData();
            }
        }

        this.id = myCard.getCard().getId();
        this.tytFlashcardId = myCard.getCard().getFlashcard().getId();
        this.frontFace = myCard.getCard().getFrontFace();
        this.backFace = myCard.getCard().getBackFace();
        this.dataFrontFace = frontImage;
        this.dataBackFace = backImage;
        this.difficultyLevel = myCard.getDifficultyLevel();
        this.isSaved = true;
    }
}
