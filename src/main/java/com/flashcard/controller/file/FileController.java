package com.flashcard.controller.file;

import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/file/admin")
@RequiredArgsConstructor
public class FileController {

    private final S3StorageService s3StorageService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        return s3StorageService.uploadFile(file, AWSDirectory.CARDS);
    }
}
