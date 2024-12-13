package com.flashcard.controller.contact.response;


import com.flashcard.model.Contact;
import lombok.Getter;

@Getter
public class ContactResponse {
    private final String message;
    private final String topic;
    private final String userName;
    private final String userSurname;
    private final Boolean seen;

    public ContactResponse(Contact contact) {
        this.message = contact.getMessage();
        this.topic = contact.getTopic();
        this.userName = contact.getUser().getUserName();
        this.userSurname = contact.getUser().getUserSurname();
        this.seen = contact.getSeen();

    }
}
