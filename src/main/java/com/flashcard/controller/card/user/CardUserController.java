package com.flashcard.controller.card.user;

import com.flashcard.controller.card.admin.response.CardResponse;
import com.flashcard.model.Card;
import com.flashcard.model.enums.DifficultyLevel;
import com.flashcard.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/card/user")
@RequiredArgsConstructor
public class CardUserController {

    private final CardService cardService;

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long flashcardId) {

        List<Card> response = cardService.getAll(flashcardId);

        List<CardResponse> tytCardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }

    @GetMapping("/explore")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> explore() {

        List<Card> response = cardService.explore();

        List<CardResponse> tytCardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }

    @GetMapping("/explore/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> exploreForMe(@RequestParam Boolean stateOfKnowledge,
                                          @RequestParam DifficultyLevel difficultyLevel) {

        List<Card> response = cardService.exploreForMe(stateOfKnowledge, difficultyLevel);

        List<CardResponse> tytCardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }


}
