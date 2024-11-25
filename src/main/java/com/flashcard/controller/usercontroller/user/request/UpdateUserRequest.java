package com.flashcard.controller.usercontroller.user.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UpdateUserRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 30)
    private String userSurname;

}
