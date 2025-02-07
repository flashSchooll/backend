package com.flashcard.controller.topic.user;

import com.flashcard.controller.topic.user.response.TopicUserResponse;
import com.flashcard.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic/user")
@RequiredArgsConstructor
public class TopicUserController {

    private final TopicService topicService;

    @GetMapping("/get-all/{lessonId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<TopicUserResponse>> getAll(@PathVariable Long lessonId) {

        List<TopicUserResponse> response = topicService.getAllByLesson(lessonId);

        return ResponseEntity.ok(response);
    }

}
