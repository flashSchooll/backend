package com.flashcard.controller.podcast.admin;

import com.flashcard.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/podcast/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class PodcastAdminController {
    private final PodcastService podcastService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // todo test edilecek çalışmıyor
    ResponseEntity<String> upload(@RequestBody File file,
                                  @RequestParam Long topicId)  {
        String path = podcastService.savePodcast(file,topicId);
        return ResponseEntity.ok(path);
    }
}
