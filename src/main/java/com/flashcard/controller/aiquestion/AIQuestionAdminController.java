package com.flashcard.controller.aiquestion;

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
@RequestMapping("/api/ai-question/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AIQuestionAdminController {
    private final AIQuestionService aiQuestionService;

    @GetMapping("get-all")
    public ResponseEntity<List<AIQuestionResponse>> getAllAIQuestions() {
        List<AIQuestionResponse> responses = aiQuestionService.findByAdmin()
                .stream().map(AIQuestionResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("get-all/{topicId}")
    public ResponseEntity<List<AIQuestionResponse>> getAllAIQuestionsWithTopic(@PathVariable Long topicId) {
        List<AIQuestionResponse> responses = aiQuestionService.findByAdminAndTopic(topicId)
                .stream().map(AIQuestionResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("{aiQuestionId}")
    public ResponseEntity<AIQuestion> getAllAIQuestionWithId(@PathVariable String aiQuestionId) {
        AIQuestion response = aiQuestionService.findById(aiQuestionId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{aiQuestionId}")
    public ResponseEntity<Void> delete(@PathVariable String aiQuestionId) {
        aiQuestionService.delete(aiQuestionId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/publish/{aiQuestionId}")
    public ResponseEntity<AIQuestionResponse> publishAIQuestion(@PathVariable String aiQuestionId) {
        AIQuestion aiQuestion = aiQuestionService.publishAIQuestion(aiQuestionId);

        return ResponseEntity.ok(new AIQuestionResponse(aiQuestion));
    }


}
