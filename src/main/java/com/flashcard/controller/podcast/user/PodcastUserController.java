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

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<PodcastUserResponse>> getPodcastByTopicId(@PathVariable("topicId") Long topicId) {
        List<PodcastUserResponse> responses = podcastService.getByTopic(topicId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PodcastUserResponse>> getPodcast() {
        List<PodcastUserResponse> responses = podcastService.getAll();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{podcastId}")
    public ResponseEntity<PodcastUserResponse> getPodcastById(@PathVariable("podcastId") Long podcastId) {
        PodcastUserResponse podcast = podcastService.getById(podcastId);

        return ResponseEntity.ok(podcast);
    }

}
