package com.flashcard.controller.card.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.card.admin.request.CardSaveAllRequest;
import com.flashcard.controller.card.admin.request.CardSaveRequest;
import com.flashcard.controller.card.admin.request.CardUpdateRequest;
import com.flashcard.controller.card.admin.response.CardResponse;
import com.flashcard.model.Card;
import com.flashcard.model.Flashcard;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/card/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CardAdminController {

    private final CardService cardService;
    private final FlashCardRepository flashCardRepository;

    @PostMapping
    public ResponseEntity<CardResponse> save(@RequestPart @Valid CardSaveRequest cardSaveRequest,
                                             @RequestPart(required = false) MultipartFile frontFile,
                                             @RequestPart(required = false) MultipartFile backFile) throws IOException {

        Card cardResponse = cardService.save(cardSaveRequest, frontFile, backFile);

        CardResponse response = new CardResponse(cardResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-all/{flashcardId}")
    public ResponseEntity<List<CardResponse>> saveAll(@RequestBody @Valid CardSaveAllRequest request,
                                                      @PathVariable Long flashcardId) throws IOException {

        List<Card> cardResponses = cardService.saveAll(flashcardId, request);

        List<CardResponse> response = cardResponses.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(@PathVariable Long id,
                                               @RequestPart CardUpdateRequest cardUpdateRequest,
                                               @RequestPart(required = false) MultipartFile frontFile,
                                               @RequestPart(required = false) MultipartFile backFile) throws IOException {

        Card response = cardService.update(cardUpdateRequest, id, frontFile, backFile);

        CardResponse cardResponse = new CardResponse(response);

        return ResponseEntity.ok(cardResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        cardService.delete(id);

        return ResponseEntity.ok("Card başarıyla silindi");
    }

    @DeleteMapping("/front-path/{id}")
    public ResponseEntity<String> deleteFrontPath(@PathVariable Long id) {

        cardService.deleteFrontPath(id);

        return ResponseEntity.ok("Resim başarıyla silindi");
    }

    @DeleteMapping("/back-path/{id}")
    public ResponseEntity<String> deleteBackPath(@PathVariable Long id) {

        cardService.deleteBackPath(id);

        return ResponseEntity.ok("Resim başarıyla silindi");
    }

    @GetMapping("/get-all/{flashcardId}")
    public ResponseEntity<List<CardResponse>> getAll(@PathVariable Long flashcardId) {
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        List<Card> response = cardService.getAll(flashcard);

        List<CardResponse> cardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(cardResponses);
    }
}
