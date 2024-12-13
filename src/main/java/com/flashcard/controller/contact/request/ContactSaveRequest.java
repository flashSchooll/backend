package com.flashcard.controller.contact.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactSaveRequest {
    @NotBlank
    private String message;

    @NotBlank
    private String topic;
}
