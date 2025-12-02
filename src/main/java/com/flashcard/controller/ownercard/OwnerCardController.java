package com.flashcard.controller.ownercard;

import com.flashcard.controller.ownercard.request.OwnerCardSaveRequest;
import com.flashcard.controller.ownercard.request.OwnerCardUpdateRequest;
import com.flashcard.controller.ownercard.response.OwnerCardResponse;
import com.flashcard.model.OwnerCard;
import com.flashcard.service.OwnerCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/owner-card/user")
@RequiredArgsConstructor
public class OwnerCardController {

    private final OwnerCardService ownerCardService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OwnerCardResponse> save(@RequestBody @Valid OwnerCardSaveRequest request) {

        OwnerCard ownerFlashcard = ownerCardService.save(request);

        OwnerCardResponse response = new OwnerCardResponse(ownerFlashcard);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OwnerCardResponse> update(@RequestBody @Valid OwnerCardUpdateRequest request) {

        OwnerCard ownerFlashcard = ownerCardService.update(request);

        OwnerCardResponse response = new OwnerCardResponse(ownerFlashcard);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        ownerCardService.delete(id);

        return ResponseEntity.ok("Başarıyla silindi");
    }

    @GetMapping("/get-all/{ownerFlashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<OwnerCardResponse>> getAll(@PathVariable Long ownerFlashcardId) {

        List<OwnerCard> ownerCards = ownerCardService.getByOwnerFlashcard(ownerFlashcardId);

        List<OwnerCardResponse> responses = ownerCards.stream()
                .map(OwnerCardResponse::new)
                .sorted(Comparator.comparing(OwnerCardResponse::getCreatedDate).reversed())
                .toList();

        return ResponseEntity.ok(responses);
    }
}
