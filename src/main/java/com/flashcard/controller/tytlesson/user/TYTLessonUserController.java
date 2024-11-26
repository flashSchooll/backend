package com.flashcard.controller.tytlesson.user;

import com.flashcard.controller.tytlesson.user.response.TYTLessonCardSeenCountResponse;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.service.TYTFlashCardService;
import com.flashcard.service.TYTLessonService;
import com.flashcard.service.UserCardPercentageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/lesson/user")
@RequiredArgsConstructor
public class TYTLessonUserController {

    private final TYTLessonService tytLessonService;
    private final TYTFlashCardService tytFlashCardService;
    private final UserCardPercentageService userCardPercentageService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getId() {

        Map<String, Long> response = tytFlashCardService.getAllByLesson();

        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getAllTYT();

        List<TYTLessonCardSeenCountResponse> responses = userCardPercentageList.stream().map(TYTLessonCardSeenCountResponse::new).toList();

        return ResponseEntity.ok(responses);
    }
}
