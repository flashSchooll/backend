package com.flashcard.controller.aiquestion;

import com.flashcard.controller.aiquestion.request.AIQuestionAdminResponse;
import com.flashcard.controller.aiquestion.request.AIQuestionUpdateRequest;
import com.flashcard.controller.aiquestion.response.AIQuestionResponse;
import com.flashcard.model.AIQuestion;
import com.flashcard.service.AIQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ai-question/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AIQuestionAdminController {
    private final AIQuestionService aiQuestionService;

    @GetMapping("get-all")
    public ResponseEntity<List<AIQuestionAdminResponse>> getAllAIQuestions() {

        // 1) Tüm kayıtları çek
        List<AIQuestion> rawList = aiQuestionService.findByAdmin();

        // 2) UUID’ye göre grupla
        Map<String, List<AIQuestion>> grouped = rawList.stream()
                .collect(Collectors.groupingBy(AIQuestion::getUuid));

        // 3) Her grup için bir AIQuestionAdminResponse üret
        List<AIQuestionAdminResponse> response = grouped.values().stream()
                .map(list -> new AIQuestionAdminResponse(list.get(0), list))
                .toList();

        return ResponseEntity.ok(response);
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
    public ResponseEntity<Void> publishAIQuestion(@PathVariable String aiQuestionId) {
        aiQuestionService.publishAIQuestion(aiQuestionId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{aiQuestionId}")
    public ResponseEntity<AIQuestionResponse> updateAIQuestion(@PathVariable String aiQuestionId,
                                                               @RequestBody AIQuestionUpdateRequest aiQuestionUpdateRequest) {
        AIQuestion aiQuestion = aiQuestionService.update(aiQuestionId, aiQuestionUpdateRequest);

        return ResponseEntity.ok(new AIQuestionResponse(aiQuestion));
    }


}
