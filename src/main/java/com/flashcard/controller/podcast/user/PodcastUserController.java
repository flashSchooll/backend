package com.flashcard.controller.podcast.user;

import com.flashcard.controller.podcast.user.response.PodcastUserResponse;
import com.flashcard.model.Podcast;
import com.flashcard.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/podcast/user")
@PreAuthorize("hasRole('USER,ADMIN')")
@RequiredArgsConstructor
public class PodcastUserController {
    private final PodcastService podcastService;

    @GetMapping("/{topicId}")
    public ResponseEntity<List<PodcastUserResponse>> getPodcastByTopicId(@PathVariable("topicId") Long topicId) {
        List<PodcastUserResponse> responses = podcastService.getByTopic(topicId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{podcastId}")
    public ResponseEntity<PodcastUserResponse> getPodcastById(@PathVariable("podcastId") Long podcastId) {
        Podcast podcast = podcastService.getById(podcastId);
        PodcastUserResponse podcastUserResponses = new PodcastUserResponse(podcast,false);

        return ResponseEntity.ok(podcastUserResponses);
    }

    @PostMapping("/save/{podcastId}")
    public ResponseEntity<String> savePodcast(@PathVariable Long podcastId) {
        podcastService.saveForUser(podcastId);

        return ResponseEntity.ok("Podcast Saved");
    }
}
