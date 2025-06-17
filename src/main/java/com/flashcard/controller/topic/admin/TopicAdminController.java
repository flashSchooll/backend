package com.flashcard.controller.topic.admin;


import com.flashcard.constants.Constants;
import com.flashcard.controller.topic.admin.request.TopicSaveRequest;
import com.flashcard.controller.topic.admin.request.TopicUpdateRequest;
import com.flashcard.controller.topic.admin.response.TopicAdminResponse;
import com.flashcard.model.Topic;
import com.flashcard.model.enums.YKSLesson;
import com.flashcard.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic/admin")
@RequiredArgsConstructor
public class TopicAdminController {

    private final TopicService topicService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TopicAdminResponse> save(@RequestBody @Valid TopicSaveRequest topicSaveRequest) {

        Topic topic = topicService.save(topicSaveRequest);

        TopicAdminResponse response = new TopicAdminResponse(topic);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TopicAdminResponse> update(@RequestBody @Valid TopicUpdateRequest topicUpdateRequest) {

        Topic topic = topicService.update(topicUpdateRequest);

        TopicAdminResponse response = new TopicAdminResponse(topic);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        topicService.delete(id);

        return ResponseEntity.ok(Constants.TOPIC_SUCCESSFULLY_DELETE);
    }


    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TopicAdminResponse>> getAll(@RequestParam(required = false) Long lessonId,
                                                           //  @RequestParam(required = false) String search,
                                                           @RequestParam(required = false) YKSLesson yksLesson,
                                                           @PageableDefault Pageable pageable) {

        Page<Topic> tytTopics;

        //  if (lessonId == null) {
        //      tytTopics = topicService.getAll(pageable);
        //  } else {
        //      tytTopics = topicService.getAllWithLesson(lessonId, pageable);
        //  }

        tytTopics = topicService.getBySearch(lessonId, yksLesson, pageable);

        Page<TopicAdminResponse> response = tytTopics.map(TopicAdminResponse::new);

        return ResponseEntity.ok(response);
    }


}
