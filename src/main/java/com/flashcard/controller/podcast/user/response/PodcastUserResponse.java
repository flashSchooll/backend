package com.flashcard.controller.podcast.user.response;

import com.flashcard.model.Podcast;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PodcastUserResponse {
    private final String path;
    private final String title;
    private final Long topicId;
    private final Integer duration;
    private final boolean seen; // Final olarak kalır ama constructor'da set edilir
    private final String photoPath;
    private final Long podcastId;
    private final boolean isSaved;
    private final String topicTitle;

    // Constructor'a seen parametresi eklendi
    public PodcastUserResponse(Podcast podcast, boolean seen, boolean isSaved) {
        this.path = podcast.getPath();
        this.title = podcast.getTitle();
        this.topicId = podcast.getTopic().getId();
        this.duration = podcast.getDuration();
        this.seen = seen; // Dışarıdan gelen seen değeri
        this.photoPath = podcast.getPhotoPath();
        this.podcastId = podcast.getId();
        this.isSaved = isSaved;
        this.topicTitle = podcast.getTopic().getSubject();
    }
}
