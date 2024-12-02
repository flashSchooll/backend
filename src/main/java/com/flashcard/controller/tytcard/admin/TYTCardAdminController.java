package com.flashcard.controller.tytcard.admin;

import com.flashcard.controller.tytcard.admin.request.TYTCardSaveAllRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardSaveRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardUpdateRequest;
import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.model.TYTCard;
import com.flashcard.service.TYTCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/card/admin")
@RequiredArgsConstructor
public class TYTCardAdminController {

    private final TYTCardService tytCardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody @Valid TYTCardSaveRequest tytCardSaveRequest) throws IOException {

        TYTCardResponse response = tytCardService.save(tytCardSaveRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-all/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveAll(@RequestBody @Valid TYTCardSaveAllRequest request,
                                     @PathVariable Long flashcardId) throws IOException {

        List<TYTCardResponse> response = tytCardService.saveAll(flashcardId,request);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody TYTCardUpdateRequest tytCardUpdateRequest) throws IOException {

        TYTCardResponse response = tytCardService.update(tytCardUpdateRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytCardService.delete(id);

        return ResponseEntity.ok("Card başarıyla silindi");
    }

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long flashcardId) {

        List<TYTCard> response = tytCardService.getAll(flashcardId);

        List<TYTCardResponse> cardResponses = response.stream().map(TYTCardResponse::new).toList();

        return ResponseEntity.ok(cardResponses);
    }
}
