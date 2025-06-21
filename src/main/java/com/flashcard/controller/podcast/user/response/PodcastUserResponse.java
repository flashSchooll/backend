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

    public PodcastUserResponse(Podcast podcast) {
        this.path = podcast.getPath();
        this.title = podcast.getTitle();
        this.topicId = podcast.getTopic().getId();
        this.duration = podcast.getDuration();
    }
}
