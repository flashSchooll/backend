package com.flashcard.controller.mypodcast.response;

import com.flashcard.model.Podcast;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PodcastResponse {
    private final String path;
    private final String title;
    private final Long topicId;
    private final Integer duration;
    private final boolean seen; // Final olarak kalır ama constructor'da set edilir
    private final String photoPath;
    private final Long podcastId;
    private final boolean isSaved;
    private final String topicTitle;

    public PodcastResponse(Podcast podcast, boolean seen, boolean isSaved) {
        this.podcastId = podcast.getId();
        this.path = podcast.getPath();
        this.photoPath = podcast.getPhotoPath();
        this.title = podcast.getTitle();
        this.topicId = podcast.getTopic().getId();
        this.duration = podcast.getDuration();
        this.seen = seen;
        this.isSaved = isSaved;
        this.topicTitle = podcast.getTopic().getSubject();
    }
}
