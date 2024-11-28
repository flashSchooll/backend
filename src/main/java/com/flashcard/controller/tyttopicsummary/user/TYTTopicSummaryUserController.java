package com.flashcard.controller.tyttopicsummary.user;

import com.flashcard.controller.tyttopicsummary.response.TYTTopicSummaryResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTTopicSummaryService;
import lombok.RequiredArgsConstructor;
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
    public ResponseObject get(@PathVariable Long id) {

        TYTTopicSummaryResponse response = tytTopicSummaryService.get(id);

        return ResponseObject.ok(response);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseObject getAll(@PathVariable Long topicId) {

        List<TYTTopicSummaryResponse> response = tytTopicSummaryService.getAllByTopic(topicId);

        return ResponseObject.ok(response);
    }
}
