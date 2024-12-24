package com.flashcard.controller.card.user;

import com.flashcard.controller.card.admin.response.CardResponse;
import com.flashcard.model.Card;
import com.flashcard.model.MyCard;
import com.flashcard.service.CardService;
import com.flashcard.service.MyCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/card/user")
@RequiredArgsConstructor
public class CardUserController {

    private final CardService cardService;
    private final MyCardsService myCardsService;

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> getAll(@PathVariable Long flashcardId) {

        List<Card> response = cardService.getAll(flashcardId);
        List<MyCard> myCards = myCardsService.getAll(flashcardId);

        List<CardResponse> cardResponses = response.stream().map(card -> new CardResponse(card, myCards)).toList();

        return ResponseEntity.ok(cardResponses);
    }

    @GetMapping("/explore")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> explore() {

        List<Card> response = cardService.explore();

        List<CardResponse> cardResponses = response.stream()
                .map(CardResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list;
                }));

        return ResponseEntity.ok(cardResponses);
    }

    @GetMapping("/explore/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> exploreForMe() {

        List<Card> response = cardService.exploreForMe();

        List<CardResponse> tytCardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }


}
