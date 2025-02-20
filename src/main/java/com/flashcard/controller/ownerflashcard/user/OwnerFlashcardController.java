package com.flashcard.controller.ownerflashcard.user;

import com.flashcard.controller.ownerflashcard.user.request.OwnerFlashcardSaveRequest;
import com.flashcard.controller.ownerflashcard.user.request.OwnerFlashcardUpdateRequest;
import com.flashcard.controller.ownerflashcard.user.response.OwnerFlashcardResponse;
import com.flashcard.model.OwnerFlashcard;
import com.flashcard.service.OwnerFlashcardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/owner-flashcard/user")
@RequiredArgsConstructor
public class OwnerFlashcardController {

    private final OwnerFlashcardService ownerFlashcardService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OwnerFlashcardResponse> getAll(@RequestBody @Valid OwnerFlashcardSaveRequest request) {

        OwnerFlashcard ownerFlashcard = ownerFlashcardService.save(request);

        OwnerFlashcardResponse response = new OwnerFlashcardResponse(ownerFlashcard);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OwnerFlashcardResponse> getAll(@RequestBody @Valid OwnerFlashcardUpdateRequest request) {

        OwnerFlashcard ownerFlashcard = ownerFlashcardService.update(request);

        OwnerFlashcardResponse response = new OwnerFlashcardResponse(ownerFlashcard);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> getAll(@PathVariable Long id) {

        ownerFlashcardService.delete(id);

        return ResponseEntity.ok("Başarıyla silindi");
    }
}
