package com.flashcard.controller.tytcard.user;

import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.service.TYTCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/card/user")
@RequiredArgsConstructor
public class TYTCardUserController {

        private final TYTCardService tytCardService;


    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAll(@PathVariable Long flashcardId) {

        List<TYTCardResponse> response = tytCardService.getAll(flashcardId);

        return ResponseEntity.ok(response);
    }

}
