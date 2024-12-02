package com.flashcard.controller.tyttopic.admin;


import com.flashcard.constants.Constants;
import com.flashcard.controller.tyttopic.admin.Request.TYTTopicSaveRequest;
import com.flashcard.controller.tyttopic.admin.Request.TYTTopicUpdateRequest;
import com.flashcard.controller.tyttopic.admin.Response.TYTTopicAdminResponse;
import com.flashcard.model.TYTTopic;
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

        TYTTopic topic = tytTopicService.save(tytLessonSaveRequest);

        TYTTopicAdminResponse response = new TYTTopicAdminResponse(topic);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody @Valid TYTTopicUpdateRequest tytTopicUpdateRequest) {

        TYTTopic topic = tytTopicService.update(tytTopicUpdateRequest);

        TYTTopicAdminResponse response = new TYTTopicAdminResponse(topic);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytTopicService.delete(id);

        return ResponseEntity.ok(Constants.TYT_TOPIC_SUCCESSFULLY_DELETE);
    }

    @GetMapping("/get-all/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long lessonId) {

        List<TYTTopic> tytTopics = tytTopicService.getAll(lessonId);

        List<TYTTopicAdminResponse> response = tytTopics.stream().map(TYTTopicAdminResponse::new).toList();

        return ResponseEntity.ok(response);
    }


}
