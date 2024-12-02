package com.flashcard.controller.tyttopicsummary.user;

import com.flashcard.controller.tyttopicsummary.response.TYTTopicSummaryResponse;
import com.flashcard.model.TYTTopicSummary;
import com.flashcard.service.TYTTopicSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/topic-summary/user")
@RequiredArgsConstructor
public class TYTTopicSummaryUserController {

    private final TYTTopicSummaryService tytTopicSummaryService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> get(@PathVariable Long id) {

        TYTTopicSummary topicSummary = tytTopicSummaryService.get(id);

        TYTTopicSummaryResponse response = new TYTTopicSummaryResponse(topicSummary);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<TYTTopicSummaryResponse> response = tytTopicSummaryService.getAllByTopic(topicId);

        return ResponseEntity.ok(response);
    }
}
