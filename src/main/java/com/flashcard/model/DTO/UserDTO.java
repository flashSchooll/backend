package com.flashcard.model.DTO;

import com.flashcard.model.User;
import lombok.Getter;

@Getter
public class UserDTO {

    private final String username;
    private final String userSurname;
    private final String email;

    public UserDTO(User user) {
        this.username = user.getUserName();
        this.userSurname = user.getUserSurname();
        this.email = user.getEmail();
    }
}
