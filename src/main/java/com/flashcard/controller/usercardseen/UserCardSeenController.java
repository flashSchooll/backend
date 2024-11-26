package com.flashcard.controller.usercardseen;

import com.flashcard.controller.usercardseen.request.UserCardSeenSaveRequest;
import com.flashcard.controller.usercardseen.response.UserCardSeenResponse;
import com.flashcard.model.UserSeenCard;
import com.flashcard.service.UserCardSeenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/user-card-seen")
@RequiredArgsConstructor
public class UserCardSeenController {

    private final UserCardSeenService userCardSeenService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> save(@RequestBody @Valid UserCardSeenSaveRequest userCardSeenSaveRequest) {

        userCardSeenService.save(userCardSeenSaveRequest);

        return ResponseEntity.ok("Kartlar başarıyla kaydedildi");
    }

    @GetMapping("/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> get(@PathVariable Long flashcardId) {

        List<UserSeenCard> response = userCardSeenService.getAllSeenCardsByFlashcard(flashcardId);

        List<UserCardSeenResponse> responseList = response.stream().map(UserCardSeenResponse::new).toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/unknown/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getUnknown(@PathVariable Long flashcardId) {

        List<UserSeenCard> response = userCardSeenService.getUnknownSeenCardsByFlashcard(flashcardId);

        List<UserCardSeenResponse> responseList = response.stream().map(UserCardSeenResponse::new).toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/known/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getKnown(@PathVariable Long flashcardId) {

        List<UserSeenCard> response = userCardSeenService.getKnownSeenCardsByFlashcard(flashcardId);

        List<UserCardSeenResponse> responseList = response.stream().map(UserCardSeenResponse::new).toList();

        return ResponseEntity.ok(responseList);
    }

}
