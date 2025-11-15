package com.flashcard.controller.avatar.admin.response;

import com.flashcard.model.Avatar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvatarResponseDto {
    private final String id;
    private final String path;

    public AvatarResponseDto(Avatar avatar) {
        this.id = avatar.getId();
        this.path = avatar.getPath();
    }
}
