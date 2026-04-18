package com.flashcard.controller.growingreport.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GrowingReportLessonResponse {
    private String lesson;
    private List<GrowingReportTopicResponse> topics;
}


