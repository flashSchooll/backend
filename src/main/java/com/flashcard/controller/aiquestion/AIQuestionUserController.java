package com.flashcard.controller.aiquestion;

import com.flashcard.controller.aiquestion.request.AIQuestionSaveRequest;
import com.flashcard.controller.aiquestion.response.AIQuestionResponse;
import com.flashcard.model.AIQuestion;
import com.flashcard.service.AIQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ai-question/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class AIQuestionUserController {
    private final AIQuestionService aiQuestionService;

    @PostMapping
    public void generateAIQuestions(@RequestBody List<AIQuestionSaveRequest> aiQuestionSaveRequest) {
        aiQuestionService.saveAll(aiQuestionSaveRequest);
    }

    @GetMapping("get-all")
    public ResponseEntity<List<AIQuestionResponse>> getAllAIQuestions() {
        List<AIQuestion> questions = aiQuestionService.findAll();
        List<AIQuestionResponse> responses = questions.stream().map(AIQuestionResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("topic/{topicId}")
    public ResponseEntity<List<AIQuestionResponse>> getAllAIQuestionsByTopic(@PathVariable Long topicId) {
        List<AIQuestion> questions = aiQuestionService.findByTopic(topicId);
        List<AIQuestionResponse> responses = questions.stream().map(AIQuestionResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{aiQuestionId}")
    public ResponseEntity<AIQuestionResponse> getAllAIQuestionsById(@PathVariable String aiQuestionId) {
        AIQuestion questions = aiQuestionService.findById(aiQuestionId);
        AIQuestionResponse responses = new AIQuestionResponse(questions);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("my-questions")
    public ResponseEntity<List<AIQuestionResponse>> getMyAIQuestions() {
        List<AIQuestion> questions = aiQuestionService.findByCurrentUser();
        List<AIQuestionResponse> responses = questions.stream().map(AIQuestionResponse::new).toList();

        return ResponseEntity.ok(responses);
    }
}
