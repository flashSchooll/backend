package com.flashcard.controller.quiz.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.quiz.request.UpdateQuizRequest;
import com.flashcard.controller.quiz.response.QuizResponse;
import com.flashcard.model.Quiz;
import com.flashcard.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quiz/admin")
@RequiredArgsConstructor
public class QuizAdminController {

    private final QuizService quizService;


    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> importExcel(@RequestBody MultipartFile file,
                                              @RequestParam Long topicId) throws Exception {

        quizService.importExcel(topicId, file);

        return ResponseEntity.ok(Constants.EXCEL_SUCCESSFULLY_IMPORTED);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAll(@PathVariable Long topicId) {

        List<QuizResponse> quizList = quizService.getByTopicAdmin(topicId);

        return ResponseEntity.ok(quizList);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getByName(@RequestParam String name, @RequestParam Long topicId) {

        List<QuizResponse> responses = quizService.getAllByName(name, topicId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-all-as-page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAll(@PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Quiz> quizList = quizService.getAll(pageable);

        Page<QuizResponse> responses = quizList.map(QuizResponse::new);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getByName(@RequestBody UpdateQuizRequest updateQuizRequest) {

        Quiz quiz = quizService.updateQuiz(updateQuizRequest);

        QuizResponse response = new QuizResponse(quiz);

        return ResponseEntity.ok(response);
    }
}
