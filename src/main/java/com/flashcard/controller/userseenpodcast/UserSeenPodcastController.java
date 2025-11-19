package com.flashcard.controller.userseenpodcast;

import com.flashcard.service.UserSeenPodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-seen-podcast")
@RequiredArgsConstructor
public class UserSeenPodcastController {

    private final UserSeenPodcastService userSeenPodcastService;

    @PostMapping
    public void create(@RequestParam Long podcastId){
        userSeenPodcastService.create(podcastId);
    }
}
