package com.flashcard.controller.repeatflashcard;

import com.flashcard.constants.Constants;
import com.flashcard.controller.repeatflashcard.response.RepeatFlashcardResponse;
import com.flashcard.model.RepeatFlashcard;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.RepeatFlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/repeat-topic")
@RequiredArgsConstructor
public class RepeatFlashcardController {

    private final RepeatFlashcardService repeatFlashcardService;

    @PostMapping("/flashcard")
    @PreAuthorize("hasRole('USER')")
    public ResponseObject save(@RequestParam Long flashcardId,
                               @RequestParam LocalDateTime repeatTime) {

        RepeatFlashcard repeatFlashcard = repeatFlashcardService.save(flashcardId, repeatTime);

        return ResponseObject.ok(repeatFlashcard);
    }

    @PostMapping("/topic")
    @PreAuthorize("hasRole('USER')")
    public ResponseObject saveByTopic(@RequestParam Long topicId,
                                      @RequestParam LocalDateTime repeatTime) {

        RepeatFlashcard repeatFlashcard = repeatFlashcardService.saveByTopic(topicId, repeatTime);

        return ResponseObject.ok(repeatFlashcard);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseObject delete(@PathVariable Long id) {

        repeatFlashcardService.delete(id);

        return ResponseObject.ok(Constants.REPEAT_CARD_SUCCESSFULLY_DELETED);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseObject getAll() {

        List<RepeatFlashcard> repeatFlashcards = repeatFlashcardService.getAll();

        List<RepeatFlashcardResponse> responses = repeatFlashcards.stream().map(RepeatFlashcardResponse::new).toList();

        return ResponseObject.ok(responses);
    }


}