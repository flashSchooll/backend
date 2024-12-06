package com.flashcard.controller.topicsummary.user;

import com.flashcard.controller.topicsummary.response.TopicSummaryResponse;
import com.flashcard.model.TopicSummary;
import com.flashcard.service.TopicSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic-summary/user")
@RequiredArgsConstructor
public class TopicSummaryUserController {

    private final TopicSummaryService topicSummaryService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> get(@PathVariable Long id) {

        TopicSummary topicSummary = topicSummaryService.get(id);

        TopicSummaryResponse response = new TopicSummaryResponse(topicSummary);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<TopicSummaryResponse> response = topicSummaryService.getAllByTopic(topicId);

        return ResponseEntity.ok(response);
    }
}
