package com.flashcard.controller.podcast.admin;

import com.flashcard.controller.podcast.admin.request.PodcastSaveRequest;
import com.flashcard.controller.podcast.admin.response.PodcastAdminResponse;
import com.flashcard.model.Podcast;
import com.flashcard.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/podcast/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class PodcastAdminController {
    private final PodcastService podcastService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestPart MultipartFile file,
                                         @RequestPart PodcastSaveRequest request) throws IOException {
        String path = podcastService.savePodcast(file, request.getTopicId(), request.getTitle());
        return ResponseEntity.ok(path);
    }

    @PutMapping("/publish/{podcastId}")
    public ResponseEntity<String> publish(@PathVariable Long podcastId) {
        podcastService.publish(podcastId);
        return ResponseEntity.ok("Successfully published podcast " + podcastId);
    }

    @PutMapping("/publish/{topicId}")
    public ResponseEntity<String> publishByTopic(@PathVariable Long topicId) {
        podcastService.publishByTopic(topicId);
        return ResponseEntity.ok("Successfully published podcast topic : " + topicId);
    }

    @GetMapping("/get-all/{topicId}")
    public ResponseEntity<List<PodcastAdminResponse>> getAll(@PathVariable Long topicId) {
        List<Podcast> podcastList = podcastService.getByTopic(topicId);
        List<PodcastAdminResponse> responses = podcastList.stream().map(PodcastAdminResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{podcastId}")
    public ResponseEntity<String> delete(@PathVariable Long podcastId) {
        podcastService.delete(podcastId);

        return ResponseEntity.ok("Successfully deleted podcast " + podcastId);
    }
}
