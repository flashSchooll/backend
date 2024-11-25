package com.flashcard.controller.tyttopicsummary;

import com.flashcard.controller.tytlessonadmin.request.TYTLessonSaveRequest;
import com.flashcard.controller.tyttopicsummary.request.TYTTopicSummarySaveRequest;
import com.flashcard.controller.tyttopicsummary.request.TYTTopicSummaryUpdateRequest;
import com.flashcard.controller.tyttopicsummary.response.TYTTopicSummaryResponse;
import com.flashcard.service.TYTTopicSummaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> save(@RequestBody @Valid TYTTopicSummarySaveRequest request) {

        tytTopicSummaryService.save(request);

        return ResponseEntity.ok("Konu özeti başarıyla kaydedildi");
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody @Valid  TYTTopicSummaryUpdateRequest request) {

        tytTopicSummaryService.update(request);

        return ResponseEntity.ok("Konu özeti başarıyla güncellendi");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytTopicSummaryService.delete(id);

        return ResponseEntity.ok("Konu özeti başarıyla silindi");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> get(@PathVariable Long id) {

        TYTTopicSummaryResponse response = tytTopicSummaryService.get(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<TYTTopicSummaryResponse> response = tytTopicSummaryService.getAllByTopic(topicId);

        return ResponseEntity.ok(response);
    }

}
