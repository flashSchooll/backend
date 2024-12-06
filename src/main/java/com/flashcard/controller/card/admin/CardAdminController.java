package com.flashcard.controller.card.admin;

import com.flashcard.controller.card.admin.request.CardSaveAllRequest;
import com.flashcard.controller.card.admin.request.CardSaveRequest;
import com.flashcard.controller.card.admin.request.CardUpdateRequest;
import com.flashcard.controller.card.admin.response.CardResponse;
import com.flashcard.model.Card;
import com.flashcard.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/card/admin")
@RequiredArgsConstructor
public class CardAdminController {

    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid CardSaveRequest tytCardSaveRequest) throws IOException {

        Card cardResponse = cardService.save(tytCardSaveRequest);

        CardResponse response = new CardResponse(cardResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-all/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveAll(@RequestBody @Valid CardSaveAllRequest request,
                                     @PathVariable Long flashcardId) throws IOException {

        List<Card> cardResponses = cardService.saveAll(flashcardId, request);

        List<CardResponse> response = cardResponses.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody CardUpdateRequest tytCardUpdateRequest) throws IOException {

        Card response = cardService.update(tytCardUpdateRequest);

        CardResponse cardResponse = new CardResponse(response);

        return ResponseEntity.ok(cardResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        cardService.delete(id);

        return ResponseEntity.ok("Card başarıyla silindi");
    }

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long flashcardId) {

        List<Card> response = cardService.getAll(flashcardId);

        List<CardResponse> cardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(cardResponses);
    }
}
