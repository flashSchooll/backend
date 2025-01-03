package com.flashcard.controller.authcontroller.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

    @Email
    private String email;
    private String operatingSystem;//todo açıklamayı kontrol et
    private String browser;
}
