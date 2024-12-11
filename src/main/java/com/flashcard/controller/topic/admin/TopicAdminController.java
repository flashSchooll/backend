package com.flashcard.controller.topic.admin;


import com.flashcard.constants.Constants;
import com.flashcard.controller.topic.admin.Request.TopicSaveRequest;
import com.flashcard.controller.topic.admin.Request.TopicUpdateRequest;
import com.flashcard.controller.topic.admin.Response.TopicAdminResponse;
import com.flashcard.model.Topic;
import com.flashcard.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic/admin")
@RequiredArgsConstructor
public class TopicAdminController {

    private final TopicService topicService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid TopicSaveRequest topicSaveRequest) {

        Topic topic = topicService.save(topicSaveRequest);

        TopicAdminResponse response = new TopicAdminResponse(topic);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody @Valid TopicUpdateRequest topicUpdateRequest) {

        Topic topic = topicService.update(topicUpdateRequest);

        TopicAdminResponse response = new TopicAdminResponse(topic);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        topicService.delete(id);

        return ResponseEntity.ok(Constants.TOPIC_SUCCESSFULLY_DELETE);
    }

    @GetMapping("/get-all/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long lessonId) {

        List<Topic> tytTopics = topicService.getAll(lessonId);

        List<TopicAdminResponse> response = tytTopics.stream().map(TopicAdminResponse::new).toList();

        return ResponseEntity.ok(response);
    }


}
