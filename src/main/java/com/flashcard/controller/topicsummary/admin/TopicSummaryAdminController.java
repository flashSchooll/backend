package com.flashcard.controller.topicsummary.admin;

import com.flashcard.controller.flashcard.admin.response.FlashcardResponse;
import com.flashcard.controller.topicsummary.request.TopicSummarySaveRequest;
import com.flashcard.controller.topicsummary.request.TopicSummaryUpdateRequest;
import com.flashcard.controller.topicsummary.response.TopicSummaryResponse;
import com.flashcard.model.TopicSummary;
import com.flashcard.service.TopicSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Object> save(@RequestBody @Valid TopicSummarySaveRequest request) {

        topicSummaryService.save(request);

        return ResponseEntity.ok("Konu özeti başarıyla kaydedildi");
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> update(@RequestBody @Valid TopicSummaryUpdateRequest request) {

        topicSummaryService.update(request);

        return ResponseEntity.ok("Konu özeti başarıyla güncellendi");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        topicSummaryService.delete(id);

        return ResponseEntity.ok("Konu özeti başarıyla silindi");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TopicSummaryResponse> get(@PathVariable Long id) {

        TopicSummary topicSummary = topicSummaryService.get(id);

        TopicSummaryResponse response = new TopicSummaryResponse(topicSummary);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TopicSummaryResponse>> getAll(@RequestParam(required = false) Long topicId,
                                                             @PageableDefault(sort = "summary", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TopicSummaryResponse> response;

        if (topicId != null) {
            response = topicSummaryService.getAllByTopic(pageable, topicId);
        } else {
            response = topicSummaryService.getAll(pageable);
        }

        return ResponseEntity.ok(response);
    }

}
