package com.flashcard.controller.card.admin.response;

import com.flashcard.model.Card;
import com.flashcard.model.MyCard;
import com.flashcard.model.enums.DifficultyLevel;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class CardResponse {

    private final Long id;
    private final Long flashcardId;
    private final String flashcardName;
    private final String frontFace;
    private final String backFace;
    private final DifficultyLevel difficultyLevel;
    private final boolean isSaved;
    private final String lesson;
    private final String yks;
    private final String frontPhotoPath;
    private final String backPhotoPath;
    private final Integer index;

    public CardResponse(Card card) {
        this.id = card.getId();
        this.flashcardId = card.getFlashcard().getId();
        this.flashcardName = card.getFlashcard().getCardName();
        this.frontFace = card.getFrontFace();
        this.backFace = card.getBackFace();
        this.difficultyLevel = null;
        this.isSaved = false;
        this.lesson = card.getFlashcard().getTopic().getLesson().getYksLesson().label;
        this.yks = card.getFlashcard().getTopic().getLesson().getYks().name();
        this.frontPhotoPath = card.getFrontPhotoPath();
        this.backPhotoPath = card.getBackPhotoPath();
        this.index = card.getIndex();
    }

    public CardResponse(Card card, List<Long> myCards) {

        this.id = card.getId();
        this.flashcardId = card.getFlashcard().getId();
        this.flashcardName = card.getFlashcard().getCardName();
        this.frontFace = card.getFrontFace();
        this.backFace = card.getBackFace();
        this.difficultyLevel = null;
        this.isSaved = myCards.contains(card.getId());
        this.lesson = card.getFlashcard().getTopic().getLesson().getYksLesson().label;
        this.yks = card.getFlashcard().getTopic().getLesson().getYks().name();
        this.frontPhotoPath = card.getFrontPhotoPath();
        this.backPhotoPath = card.getBackPhotoPath();
        this.index = card.getIndex();
    }

    public CardResponse(MyCard myCard) {
        this.id = myCard.getCard().getId();
        this.flashcardId = myCard.getCard().getFlashcard().getId();
        this.flashcardName = myCard.getCard().getFlashcard().getCardName();
        this.frontFace = myCard.getCard().getFrontFace();
        this.backFace = myCard.getCard().getBackFace();
        this.difficultyLevel = myCard.getDifficultyLevel();
        this.isSaved = true;
        this.lesson = myCard.getCard().getFlashcard().getTopic().getLesson().getYksLesson().label;
        this.yks = myCard.getCard().getFlashcard().getTopic().getLesson().getYks().name();
        this.frontPhotoPath = myCard.getCard().getFrontPhotoPath();
        this.backPhotoPath = myCard.getCard().getBackPhotoPath();
        this.index = myCard.getCard().getIndex();
    }
}
