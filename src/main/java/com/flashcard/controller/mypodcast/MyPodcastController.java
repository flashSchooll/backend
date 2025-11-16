package com.flashcard.controller.mypodcast;

import com.flashcard.controller.mypodcast.response.PodcastResponse;
import com.flashcard.model.MyPodcast;
import com.flashcard.service.MyPodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/my-podcast")
@PreAuthorize("hasRole('USER,ADMIN')")
@RequiredArgsConstructor
public class MyPodcastController {

    private final MyPodcastService myPodcastService;

    @PostMapping()
    public ResponseEntity<String> savePodcast(@RequestParam Long podcastId) {
        myPodcastService.saveForUser(podcastId);

        return ResponseEntity.ok("Podcast Saved");
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam Long podcastId) {
        myPodcastService.delete(podcastId);

        return ResponseEntity.ok("Podcast deleted");
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PodcastResponse>> getAll() {
        List<PodcastResponse> response = myPodcastService.getMyPodcasts();
        return ResponseEntity.ok(response);
    }
}
