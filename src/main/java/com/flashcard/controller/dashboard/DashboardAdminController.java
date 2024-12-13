package com.flashcard.controller.dashboard;

import com.flashcard.controller.dashboard.response.AdminDashboardResponse;
import com.flashcard.repository.*;
import com.flashcard.service.UserCardPercentageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
public class DashboardAdminController {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final TopicRepository topicRepository;
    private final TopicSummaryRepository topicSummaryRepository;
    private final FlashCardRepository flashCardRepository;
    private final CardRepository cardRepository;
    private final UserCardPercentageService userCardPercentageService;


    @GetMapping
    public ResponseEntity<AdminDashboardResponse> getParameters() {

        long countUser = userRepository.count();
        long countLesson = lessonRepository.count();
        long countTopic = topicRepository.count();
        long countTopicSummary = topicSummaryRepository.count();
        long countFlashcard = flashCardRepository.count();
        long countCard = cardRepository.count();
        long countAverageFifty = userCardPercentageService.countAverageFifty();

        AdminDashboardResponse response = AdminDashboardResponse
                .builder()
                .countUser(countUser)
                .countAverageFifty(countAverageFifty)
                .countCard(countCard)
                .countFlashcard(countFlashcard)
                .countLesson(countLesson)
                .countTopic(countTopic)
                .countTopicSummary(countTopicSummary)
                .build();

        return ResponseEntity.ok(response);
    }
}
