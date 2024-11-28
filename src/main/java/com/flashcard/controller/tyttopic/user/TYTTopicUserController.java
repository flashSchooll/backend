package com.flashcard.controller.tyttopic.user;

import com.flashcard.controller.tyttopic.user.response.TYTTopicUserResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/topic/user")
@RequiredArgsConstructor
public class TYTTopicUserController {

    private final TYTTopicService tytTopicService;

    @GetMapping("/get-all/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAll(@PathVariable Long lessonId) {

        List<TYTTopicUserResponse> response = tytTopicService.getAllUser(lessonId);

        return ResponseObject.ok(response);
    }

}
