package com.flashcard.controller.flashcard.user;

import com.flashcard.controller.flashcard.admin.response.FlashcardResponse;
import com.flashcard.controller.flashcard.user.response.FlashcardUserResponse;
import com.flashcard.model.Flashcard;
import com.flashcard.service.FlashCardService;
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

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<FlashcardUserResponse> response = flashCardService.getAllUser(topicId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam @NotBlank String search) {

        List<Flashcard> flashcards = flashCardService.search(search);

        List<FlashcardResponse> response = flashcards.stream().map(FlashcardResponse::new).toList();

        return ResponseEntity.ok(response);
    }


}
