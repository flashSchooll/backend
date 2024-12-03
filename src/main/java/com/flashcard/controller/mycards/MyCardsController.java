package com.flashcard.controller.mycards;

import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.model.MyCard;
import com.flashcard.model.TYTCard;
import com.flashcard.service.MyCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/mycards/user")
@RequiredArgsConstructor
public class MyCardsController {

    private final MyCardsService myCardsService;

    @PostMapping("/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> save(@PathVariable Long cardId) {

        TYTCard card = myCardsService.save(cardId);

        TYTCardResponse response = new TYTCardResponse(card);

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
    public ResponseEntity<?> getAll() {

        List<MyCard> myCards = myCardsService.getAll();

        List<TYTCardResponse> responses = myCards.stream()
                .map(myCard -> new TYTCardResponse(myCard.getTytCard())).toList();

        return ResponseEntity.ok(responses);
    }
}
