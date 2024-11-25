package com.flashcard.controller.authcontroller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 40)
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Lütfen Geçerli bir şifre giriniz")
    private String password;

}
