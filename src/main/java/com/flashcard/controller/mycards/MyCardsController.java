package com.flashcard.controller.mycards;

import com.flashcard.controller.card.admin.response.CardResponse;
import com.flashcard.model.MyCard;
import com.flashcard.model.Card;
import com.flashcard.model.enums.DifficultyLevel;
import com.flashcard.service.MyCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/mycards/user")
@RequiredArgsConstructor
public class MyCardsController {

    private final MyCardsService myCardsService;

    @PostMapping("/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> save(@PathVariable Long cardId) {

        Card card = myCardsService.save(cardId);

        CardResponse response = new CardResponse(card);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long cardId) {

        myCardsService.delete(cardId);

        return ResponseEntity.ok("Kartlarımdan başarıyla kaldırıldı");
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(required = false) DifficultyLevel difficultyLevel) {

        List<MyCard> myCards = myCardsService.getAll(difficultyLevel);

        List<CardResponse> responses = myCards.stream()
                .map(myCard -> new CardResponse(myCard.getCard())).toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/level/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> saveWithLevel(@PathVariable Long cardId,
                                           @RequestParam String difficultyLevel) {

        MyCard card = myCardsService.saveWithLevel(cardId, difficultyLevel);

        CardResponse response = new CardResponse(card);

        return ResponseEntity.ok(response);
    }

}
