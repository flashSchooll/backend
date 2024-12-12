package com.flashcard.controller.statistic.response;

public record UserStatisticLessonResponse(String lesson, Long seenCard, Double percentage) {

}
