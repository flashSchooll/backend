package com.flashcard.controller.podcast.admin.response;

import com.flashcard.model.Podcast;
import com.flashcard.model.Topic;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PodcastAdminResponse {

    private final Long id;
    private final Long topicId;
    private final String topicName;
    private final String path;
    private final String title;
    private final Integer duration;
    private final boolean published;

    public PodcastAdminResponse(Podcast podcast) {
        this.id = podcast.getId();
        this.topicId = podcast.getTopic().getId();
        this.title = podcast.getTitle();
        this.path = podcast.getPath();
        this.duration = podcast.getDuration();
        this.published = podcast.isPublished();
        this.topicName = podcast.getTopic().getSubject();
    }
}
