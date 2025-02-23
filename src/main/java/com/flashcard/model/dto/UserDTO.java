package com.flashcard.model.dto;

import com.flashcard.model.User;
import com.flashcard.model.enums.Branch;
import lombok.Getter;

@Getter
public class UserDTO {

    private final String username;
    private final String userSurname;
    private final String email;
    //  private final byte[] profilePhoto;
    private final Integer star;
    private final Integer rosette;
    private final Branch branch;
    private final String photoPath;

    public UserDTO(User user) {
        this.username = user.getUserName();
        this.userSurname = user.getUserSurname();
        this.email = user.getEmail();
        //   this.profilePhoto = user.getProfilePhoto();
        this.star = user.getStar();
        this.rosette = user.getRosette();
        this.branch = user.getBranch();
        this.photoPath = user.getPhotoPath();
    }
}
