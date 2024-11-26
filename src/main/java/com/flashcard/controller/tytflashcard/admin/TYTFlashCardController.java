package com.flashcard.controller.tytflashcard.admin;


import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardSaveRequest;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardUpdateRequest;
import com.flashcard.controller.tytflashcard.admin.response.TYTFlashcardResponse;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.service.TYTFlashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/flashcard/admin")
@RequiredArgsConstructor
public class TYTFlashCardController {

    private final TYTFlashCardService tytFlashCardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody TYTFlashcardSaveRequest tytFlashcardSaveRequest) {

        TYTFlashcardResponse response = tytFlashCardService.save(tytFlashcardSaveRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody TYTFlashcardUpdateRequest tytFlashcardUpdateRequest) {

        TYTFlashcardResponse response = tytFlashCardService.update(tytFlashcardUpdateRequest);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytFlashCardService.delete(id);

        return ResponseEntity.ok("Flashcard başarıyla silindi");
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<TYTFlashcardResponse> response = tytFlashCardService.getAll(topicId)
                .stream().map(TYTFlashcardResponse::new).toList();

        return ResponseEntity.ok(response);
    }
}
