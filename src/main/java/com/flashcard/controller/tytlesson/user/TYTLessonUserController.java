package com.flashcard.controller.tytlesson.user;

import com.flashcard.controller.tytlesson.user.response.TYTLessonCardSeenCountResponse;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.service.UserCardPercentageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/lesson/user")
@RequiredArgsConstructor
public class TYTLessonUserController {

    private final UserCardPercentageService userCardPercentageService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getId() {

        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getAllTYT();

        List<TYTLessonCardSeenCountResponse> responses = userCardPercentageList.stream().map(TYTLessonCardSeenCountResponse::new).toList();

        return ResponseEntity.ok(responses);
    }
}
