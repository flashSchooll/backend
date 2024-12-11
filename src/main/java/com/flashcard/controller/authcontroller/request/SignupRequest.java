package com.flashcard.controller.authcontroller.request;

import jakarta.validation.constraints.*;
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
  @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
          message = "Lütfen Geçerli bir şifre giriniz")
  private String password;

  @NotBlank
  @Size(max = 30)
  private String userSurname;

  private Set<String> role;

  @NotNull
  private Boolean userAgreement;

}
