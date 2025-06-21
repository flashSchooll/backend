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
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class PodcastUserController {
    private final PodcastService podcastService;

    @GetMapping("/{topicId}")
    public ResponseEntity<List<PodcastUserResponse>> getPodcastByTopicId(@PathVariable("topicId") Long topicId) {
        List<Podcast> podcastList = podcastService.getByTopic(topicId);
        List<PodcastUserResponse> podcastUserResponses = podcastList.stream().map(PodcastUserResponse::new).toList();

        return ResponseEntity.ok(podcastUserResponses);
    }
}
