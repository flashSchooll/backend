package com.flashcard.model.DTO;

import com.flashcard.model.User;
import lombok.Getter;

@Getter
public class UserDTO {

    private final String username;
    private final String userSurname;
    private final String email;
    private final byte[] profilePhoto;
    private final Integer star;
    private final Integer rosette;

    public UserDTO(User user) {
        this.username = user.getUserName();
        this.userSurname = user.getUserSurname();
        this.email = user.getEmail();
        this.profilePhoto = user.getProfilePhoto();
        this.star = user.getStar();
        this.rosette = user.getRosette();
    }
}
