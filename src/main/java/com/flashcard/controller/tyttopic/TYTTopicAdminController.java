package com.flashcard.controller.tyttopic;


import com.flashcard.controller.tyttopic.Request.TYTTopicSaveRequest;
import com.flashcard.controller.tyttopic.Request.TYTTopicUpdateRequest;
import com.flashcard.controller.tyttopic.Response.TYTTopicAdminResponse;
import com.flashcard.service.TYTTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/topic/admin")
@RequiredArgsConstructor
public class TYTTopicAdminController {

    private final TYTTopicService tytTopicService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid TYTTopicSaveRequest tytLessonSaveRequest) {

        TYTTopicAdminResponse response = tytTopicService.save(tytLessonSaveRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody @Valid TYTTopicUpdateRequest tytTopicUpdateRequest) {

        TYTTopicAdminResponse response = tytTopicService.update(tytTopicUpdateRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytTopicService.delete(id);

        return ResponseEntity.ok("Konu başarıyla silindi");
    }

    @GetMapping("/get-all/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long lessonId) {

        List<TYTTopicAdminResponse> response = tytTopicService.getAll(lessonId);

        return ResponseEntity.ok(response);
    }


}
