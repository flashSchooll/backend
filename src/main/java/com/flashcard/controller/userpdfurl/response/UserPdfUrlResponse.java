package com.flashcard.controller.userpdfurl.response;

import com.flashcard.model.UserPdfUrl;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class UserPdfUrlResponse {
    private final UUID uuid;
    private final String subject;
    private final String url;

    public UserPdfUrlResponse(UserPdfUrl userPdfUrl) {
        this.uuid = userPdfUrl.getId();
        this.subject = userPdfUrl.getSubject();
        this.url = userPdfUrl.getUrl();
    }
}
