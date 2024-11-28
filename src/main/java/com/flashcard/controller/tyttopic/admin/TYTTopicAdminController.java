package com.flashcard.controller.tyttopic.admin;


import com.flashcard.controller.tyttopic.admin.Request.TYTTopicSaveRequest;
import com.flashcard.controller.tyttopic.admin.Request.TYTTopicUpdateRequest;
import com.flashcard.controller.tyttopic.admin.Response.TYTTopicAdminResponse;
import com.flashcard.payload.response.ResponseObject;
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
    public ResponseObject save(@RequestBody @Valid TYTTopicSaveRequest tytLessonSaveRequest) {

        TYTTopicAdminResponse response = tytTopicService.save(tytLessonSaveRequest);

        return ResponseObject.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject update(@RequestBody @Valid TYTTopicUpdateRequest tytTopicUpdateRequest) {

        TYTTopicAdminResponse response = tytTopicService.update(tytTopicUpdateRequest);

        return ResponseObject.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject delete(@PathVariable Long id) {

        tytTopicService.delete(id);

        return ResponseObject.ok("Konu başarıyla silindi");
    }

    @GetMapping("/get-all/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAll(@PathVariable Long lessonId) {

        List<TYTTopicAdminResponse> response = tytTopicService.getAll(lessonId);

        return ResponseObject.ok(response);
    }


}
