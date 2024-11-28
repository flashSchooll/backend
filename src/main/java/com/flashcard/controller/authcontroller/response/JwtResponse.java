package com.flashcard.controller.authcontroller.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String surname;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String surname, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.surname = surname;
    }
}
