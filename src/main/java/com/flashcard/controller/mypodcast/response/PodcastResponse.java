package com.flashcard.controller.mypodcast.response;

import com.flashcard.model.Podcast;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PodcastResponse {
    private final Long id;
    private final String path;
    private final String photoPath;
    private final String title;
    private final String topic;

    public PodcastResponse(Podcast podcast) {
        this.id = podcast.getId();
        this.path = podcast.getPath();
        this.photoPath = podcast.getPhotoPath();
        this.title = podcast.getTitle();
        this.topic = podcast.getTopic().getSubject();
    }
}
