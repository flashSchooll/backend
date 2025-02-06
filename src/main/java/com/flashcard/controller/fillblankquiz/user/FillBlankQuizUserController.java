package com.flashcard.controller.fillblankquiz.user;

import com.flashcard.controller.fillblankquiz.admin.response.FillBlankQuizResponse;
import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import com.flashcard.model.FillBlankQuiz;
import com.flashcard.service.FillBlankQuizService;
import com.flashcard.service.UserFillBlankQuizService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fill-blank-quiz/user")
@RequiredArgsConstructor
public class FillBlankQuizUserController {

    private final FillBlankQuizService fillBlankQuizService;
    private final UserFillBlankQuizService userFillBlankQuizService;

    @GetMapping("/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Object> getByTopic(@PathVariable Long topicId) {

        List<FillBlankQuizUserResponse> responses = fillBlankQuizService.getCountByUser(topicId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/title")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Object> getByTitle(@RequestParam String title) {

        List<FillBlankQuiz> fillBlankQuizList = fillBlankQuizService.getByTitle(title);

        List<FillBlankQuizResponse> responses = fillBlankQuizList.stream().map(FillBlankQuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<String> saveUserFillBlank(@RequestParam @NotBlank String title,
                                                    @RequestParam @NotNull Long topicId,
                                                    @RequestParam Integer known) {

        userFillBlankQuizService.save(title, topicId, known);

        return ResponseEntity.ok("Boşluk doldurmalı soru cevapları başarıyla kaydedildi");
    }

}
