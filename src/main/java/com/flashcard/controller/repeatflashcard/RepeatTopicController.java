package com.flashcard.controller.repeatflashcard;

import com.flashcard.constants.Constants;
import com.flashcard.controller.repeatflashcard.response.RepeatFlashcardResponse;
import com.flashcard.model.RepeatFlashcard;
import com.flashcard.service.RepeatFlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/repeat-topic")
@RequiredArgsConstructor
public class RepeatTopicController {

    private final RepeatFlashcardService repeatFlashcardService;

    @PostMapping("/flashcard")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RepeatFlashcardResponse> save(@RequestParam Long flashcardId,
                                                        @RequestParam LocalDateTime repeatTime) {

        RepeatFlashcard repeatFlashcard = repeatFlashcardService.save(flashcardId, repeatTime);
        RepeatFlashcardResponse response = new RepeatFlashcardResponse(repeatFlashcard, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/topic")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RepeatFlashcardResponse> saveByTopic(@RequestParam Long topicId,
                                                               @RequestParam LocalDateTime repeatTime) {

        RepeatFlashcard repeatFlashcard = repeatFlashcardService.saveByTopic(topicId, repeatTime);

        RepeatFlashcardResponse response = new RepeatFlashcardResponse(repeatFlashcard, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        repeatFlashcardService.delete(id);

        return ResponseEntity.ok(Constants.REPEAT_CARD_SUCCESSFULLY_DELETED);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RepeatFlashcardResponse>> getAll() {

        List<RepeatFlashcardResponse> responses = repeatFlashcardService.getAll();

        return ResponseEntity.ok(responses);
    }


}