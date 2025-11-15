package com.flashcard.controller.avatar.user;

import com.flashcard.controller.avatar.admin.response.AvatarResponseDto;
import com.flashcard.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/avatar/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AvatarUserController {

    private final AvatarService avatarService;

    @GetMapping("/getAll")
    public List<AvatarResponseDto> getAllAvatar() {
        return avatarService.getAll();
    }
}
