package com.flashcard.controller.topicsummary.admin;

import com.flashcard.controller.topicsummary.request.TopicSummarySaveRequest;
import com.flashcard.controller.topicsummary.request.TopicSummaryUpdateRequest;
import com.flashcard.controller.topicsummary.response.TopicSummaryResponse;
import com.flashcard.model.TopicSummary;
import com.flashcard.service.TopicSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic-summary/admin")
@RequiredArgsConstructor
public class TopicSummaryAdminController {

    private final TopicSummaryService topicSummaryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid TopicSummarySaveRequest request) {

        topicSummaryService.save(request);

        return ResponseEntity.ok("Konu özeti başarıyla kaydedildi");
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody @Valid TopicSummaryUpdateRequest request) {

        topicSummaryService.update(request);

        return ResponseEntity.ok("Konu özeti başarıyla güncellendi");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        topicSummaryService.delete(id);

        return ResponseEntity.ok("Konu özeti başarıyla silindi");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> get(@PathVariable Long id) {

        TopicSummary topicSummary = topicSummaryService.get(id);

        TopicSummaryResponse response = new TopicSummaryResponse(topicSummary);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<TopicSummaryResponse> response = topicSummaryService.getAllByTopic(topicId);

        return ResponseEntity.ok(response);
    }

}
