package com.flashcard.controller.tytcard.user;

import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.model.TYTCard;
import com.flashcard.model.enums.DifficultyLevel;
import com.flashcard.service.TYTCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/card/user")
@RequiredArgsConstructor
public class TYTCardUserController {

    private final TYTCardService tytCardService;

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long flashcardId) {

        List<TYTCard> response = tytCardService.getAll(flashcardId);

        List<TYTCardResponse> tytCardResponses = response.stream().map(TYTCardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }

    @GetMapping("/explore")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> explore() {

        List<TYTCard> response = tytCardService.explore();

        List<TYTCardResponse> tytCardResponses = response.stream().map(TYTCardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }

    @GetMapping("/explore/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> exploreForMe(@RequestParam Boolean stateOfKnowledge,
                                          @RequestParam DifficultyLevel difficultyLevel) {

        List<TYTCard> response = tytCardService.exploreForMe(stateOfKnowledge, difficultyLevel);

        List<TYTCardResponse> tytCardResponses = response.stream().map(TYTCardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }


}
