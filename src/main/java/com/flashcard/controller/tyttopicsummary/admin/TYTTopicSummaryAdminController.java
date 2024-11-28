package com.flashcard.controller.tyttopicsummary.admin;

import com.flashcard.controller.tyttopicsummary.request.TYTTopicSummarySaveRequest;
import com.flashcard.controller.tyttopicsummary.request.TYTTopicSummaryUpdateRequest;
import com.flashcard.controller.tyttopicsummary.response.TYTTopicSummaryResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTTopicSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/topic-summary/admin")
@RequiredArgsConstructor
public class TYTTopicSummaryAdminController {

    private final TYTTopicSummaryService tytTopicSummaryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject save(@RequestBody @Valid TYTTopicSummarySaveRequest request) {

        tytTopicSummaryService.save(request);

        return ResponseObject.ok("Konu özeti başarıyla kaydedildi");
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject update(@RequestBody @Valid TYTTopicSummaryUpdateRequest request) {

        tytTopicSummaryService.update(request);

        return ResponseObject.ok("Konu özeti başarıyla güncellendi");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject delete(@PathVariable Long id) {

        tytTopicSummaryService.delete(id);

        return ResponseObject.ok("Konu özeti başarıyla silindi");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject get(@PathVariable Long id) {

        TYTTopicSummaryResponse response = tytTopicSummaryService.get(id);

        return ResponseObject.ok(response);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAll(@PathVariable Long topicId) {

        List<TYTTopicSummaryResponse> response = tytTopicSummaryService.getAllByTopic(topicId);

        return ResponseObject.ok(response);
    }

}
