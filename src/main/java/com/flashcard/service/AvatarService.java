package com.flashcard.service;

import com.flashcard.controller.avatar.admin.response.AvatarResponseDto;
import com.flashcard.model.Avatar;
import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.repository.AvatarRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final S3StorageService s3StorageService;

    // ADMIN
    @Transactional
    public AvatarResponseDto create(MultipartFile file) throws IOException {
        String path = s3StorageService.uploadFile(file, AWSDirectory.AVATAR);

        Avatar avatar = new Avatar();
        avatar.setPath(path);
        avatar.setId(UUID.randomUUID().toString());

        return new AvatarResponseDto(avatarRepository.save(avatar));
    }

    @Transactional
    public void delete(String id) {
        Avatar avatar = avatarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avatar bulunamadı"));

        avatarRepository.delete(avatar);
    }

    public List<AvatarResponseDto> getAll() {
        return avatarRepository.findAll().stream().map(AvatarResponseDto::new).toList();
    }

    // USER


}
