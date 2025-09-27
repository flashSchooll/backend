package com.flashcard.controller.authcontroller.response;

import com.flashcard.model.enums.Branch;
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
    private Integer star = null;
    private Integer rosette = null;
    private Branch branch = null;
    private Integer series = null;

    public JwtResponse(String accessToken, Long id, String username, String surname, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.surname = surname;
    }
}
