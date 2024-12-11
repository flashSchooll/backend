package com.flashcard.controller.flashcard.user;

import com.flashcard.controller.flashcard.user.response.FlashcardSearchResponse;
import com.flashcard.controller.flashcard.user.response.FlashcardUserResponse;
import com.flashcard.model.Card;
import com.flashcard.model.Flashcard;
import com.flashcard.model.UserSeenCard;
import com.flashcard.service.FlashCardService;
import com.flashcard.service.UserSeenCardService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flashcard/user")
@RequiredArgsConstructor
public class FlashcardUserController {

    private final FlashCardService flashCardService;
    private final UserSeenCardService userSeenCardService;

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<FlashcardUserResponse>> getAll(@PathVariable Long topicId) {

        List<FlashcardUserResponse> response = flashCardService.getAllUser(topicId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<FlashcardSearchResponse>> search(@RequestParam @NotBlank String search) {

        List<Flashcard> flashcards = flashCardService.search(search);

        List<FlashcardSearchResponse> response = flashcards.stream().map(FlashcardSearchResponse::new).toList();

        return ResponseEntity.ok(response);
    }


}
