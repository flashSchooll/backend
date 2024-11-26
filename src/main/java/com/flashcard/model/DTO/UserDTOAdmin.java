package com.flashcard.model.DTO;

import com.flashcard.model.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserDTOAdmin {

    private final String username;
    private final String userSurname;
    private final String email;
    private final List<String> roles;
    private final LocalDateTime createdDate;
    private final Integer star;
    private final Integer rosette;

    public UserDTOAdmin(User user) {
        this.username = user.getUserName();
        this.userSurname = user.getUserSurname();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(role -> role.getName().getLabel()).toList();
        this.createdDate = user.getCreatedDate();
        this.star = user.getStar();
        this.rosette = user.getRosette();
    }
}
