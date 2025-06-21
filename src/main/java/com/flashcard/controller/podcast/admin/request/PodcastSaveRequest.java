package com.flashcard.controller.podcast.admin.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PodcastSaveRequest {
    private Long topicId;
    private String title;
}
