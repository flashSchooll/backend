package com.flashcard.controller.podcast.admin;

import com.flashcard.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/podcast/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class PodcastAdminController {
    private final PodcastService podcastService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam("topicId") Long topicId,
                                         @RequestParam("title") String title) throws IOException {
        String path = podcastService.savePodcast(file, topicId, title);
        return ResponseEntity.ok(path);
    }
}
