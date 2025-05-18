package com.flashcard.controller.errorsupport.admin.response;

import com.flashcard.model.ErrorSupport;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorSupportResponse {
    private final Long id;
    private final String errorMessage;
    private final Long cardId;
    private final String frontFace;
    private final String backFace;
    private final String flashcard;
    private final String topic;
    private final String lesson;
    private final String yks;
    private final String user;
    private final Boolean solved;
    private final LocalDateTime createdDate;
    private final LocalDateTime solvedDate;


    public ErrorSupportResponse(ErrorSupport errorSupport) {
        this.id = errorSupport.getId();
        this.errorMessage = errorSupport.getErrorMessage();
        this.cardId = errorSupport.getCard().getId();
        this.frontFace = errorSupport.getCard().getFrontFace();
        this.backFace = errorSupport.getCard().getBackFace();
        this.flashcard = errorSupport.getCard().getFlashcard().getCardName();
        this.topic = errorSupport.getCard().getFlashcard().getTopic().getSubject();
        this.lesson = errorSupport.getCard().getFlashcard().getTopic().getLesson().getYksLesson().label;
        this.yks = errorSupport.getCard().getFlashcard().getTopic().getLesson().getYks().name();
        this.user = errorSupport.getUser().getUserNameAndSurname();
        this.solved = errorSupport.getSolved();
        this.createdDate = errorSupport.getCreatedDate();
        this.solvedDate = errorSupport.getSolvedDate();

    }
}
