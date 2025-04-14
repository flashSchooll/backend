package com.flashcard.controller.fillblankquiz.user;

import com.flashcard.constants.Constants;
import com.flashcard.controller.fillblankquiz.admin.response.FillBlankQuizResponse;
import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import com.flashcard.model.FillBlankQuiz;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.repository.TopicRepository;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.FillBlankQuizService;
import com.flashcard.service.UserFillBlankQuizService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fill-blank-quiz/user")
@RequiredArgsConstructor
public class FillBlankQuizUserController {

    private final FillBlankQuizService fillBlankQuizService;
    private final UserFillBlankQuizService userFillBlankQuizService;
    private final AuthService authService;
    private final TopicRepository topicRepository;

    @GetMapping("/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Object> getByTopic(@PathVariable Long topicId) {
        User user = authService.getCurrentUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));
        List<FillBlankQuizUserResponse> responses = fillBlankQuizService.getCountByUser(user, topic);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{topicId}/title")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Object> getByTitle(@RequestParam String title,
                                             @PathVariable Long topicId) {

        List<FillBlankQuiz> fillBlankQuizList = fillBlankQuizService.getByTitleAndTopic(title, topicId);

        List<FillBlankQuizResponse> responses = fillBlankQuizList.stream().map(FillBlankQuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> saveUserFillBlank(@RequestParam @NotBlank String title,
                                                    @RequestParam @NotNull Long topicId,
                                                    @RequestParam Integer known) {

        userFillBlankQuizService.save(title, topicId, known);

        return ResponseEntity.ok("Boşluk doldurmalı soru cevapları başarıyla kaydedildi");
    }

}
