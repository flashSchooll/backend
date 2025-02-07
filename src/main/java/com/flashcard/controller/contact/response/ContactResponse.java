package com.flashcard.controller.contact.response;


import com.flashcard.model.ContactMessage;
import lombok.Getter;

@Getter
public class ContactResponse {
    private final String message;
    private final String topic;
    private final String userName;
    private final String userSurname;
    private final Boolean seen;

    public ContactResponse(ContactMessage contactMessage) {
        this.message = contactMessage.getMessage();
        this.topic = contactMessage.getTopic();
        this.userName = contactMessage.getUser().getUserName();
        this.userSurname = contactMessage.getUser().getUserSurname();
        this.seen = contactMessage.getSeen();

    }
}
