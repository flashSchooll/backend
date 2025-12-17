package com.flashcard.controller.aiquestionpool;

import com.flashcard.controller.aiquestionpool.request.AIQuestionUserSaveRequest;
import com.flashcard.controller.aiquestionpool.response.AIQuestionUserResponse;
import com.flashcard.service.AIQuestionUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ai-question-pool/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class AIQuestionPoolController {
    private final AIQuestionUserService aiQuestionPoolService;

    @PostMapping()
    public void save(@RequestBody List<AIQuestionUserSaveRequest> request) {
        aiQuestionPoolService.save(request);
    }

    @GetMapping()
    public List<AIQuestionUserResponse> getAll() {
        return aiQuestionPoolService.getUsersWrongQuestionsGroupedByTestTitle();
    }

    @PutMapping()
    public void doneQuestion(@RequestBody List<String> request) {
        aiQuestionPoolService.updateQuestions(request);
    }
}
