package com.flashcard.controller.avatar.admin;

import com.flashcard.controller.avatar.admin.response.AvatarResponseDto;
import com.flashcard.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/avatar/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping
    public AvatarResponseDto createAvatar(@RequestBody MultipartFile file) throws IOException {
        return avatarService.create(file);
    }

    @DeleteMapping("/{id}")
    public void deleteAvatar(@PathVariable String id) {
        avatarService.delete(id);
    }

    @GetMapping("/getAll")
    public List<AvatarResponseDto> getAllAvatar() {
        return avatarService.getAll();
    }
}
