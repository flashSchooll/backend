package com.flashcard.controller.authcontroller.request;

import com.flashcard.model.enums.Branch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    @NotBlank
    @Size(max = 30)
    private String userSurname;

    private Set<String> role;

    @NotNull
    private Boolean userAgreement;

    private Branch branch;

    private String avatar;
}
