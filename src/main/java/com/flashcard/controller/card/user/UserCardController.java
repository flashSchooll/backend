package com.flashcard.controller.card.user;

import com.flashcard.constants.Constants;
import com.flashcard.controller.card.admin.response.CardResponse;
import com.flashcard.model.Card;
import com.flashcard.model.Flashcard;
import com.flashcard.model.MyCard;
import com.flashcard.model.User;
import com.flashcard.model.enums.Branch;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.CardService;
import com.flashcard.service.MyCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/card/user")
@RequiredArgsConstructor
public class UserCardController {

    private final CardService cardService;
    private final MyCardsService myCardsService;
    private final AuthService authService;
    private final FlashCardRepository flashCardRepository;

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> getAll(@PathVariable Long flashcardId) {

        User user = authService.getCurrentUser();// todo burası güncellenecek servise taşınacak

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        List<Card> response = cardService.getAll(flashcard);
        List<Long> myCards = myCardsService.getAll(user.getId(), flashcard);

        List<CardResponse> cardResponses = new java.util.ArrayList<>(response.stream().map(card -> new CardResponse(card, myCards)).toList());
        cardResponses.sort(Comparator.comparingInt(CardResponse::getIndex));
        return ResponseEntity.ok(cardResponses);
    }

    @GetMapping("/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CardResponse> get(@PathVariable Long cardId) {

        Card response = cardService.getCard(cardId);

        CardResponse cardResponses = new CardResponse(response);

        return ResponseEntity.ok(cardResponses);
    }

    @GetMapping("/explore/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> exploreForMe() {

        List<Card> response = cardService.getRandomCardsFromUserCollection();

        List<CardResponse> cardResponses = response.stream()
                .map(CardResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list;
                }));

        return ResponseEntity.ok(cardResponses);
    }

    @GetMapping("/explore")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> explore() {

        User user = authService.getCurrentUser();

        Branch branch = user.getBranch();

        List<Card> response = cardService.explore(branch);
        Collections.shuffle(response);

        List<CardResponse> tytCardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }

    @GetMapping("/explore-retry")      // eğer kullanıcı listeyi bitirirse buna istek atacak
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CardResponse>> exploreRetry() {

        User user = authService.getCurrentUser();

        Branch branch = user.getBranch();

        List<Card> response = cardService.exploreWithoutCache(branch);

        List<CardResponse> tytCardResponses = response.stream().map(CardResponse::new).toList();

        return ResponseEntity.ok(tytCardResponses);
    }


}
