package com.flashcard.controller.usercardseen;

import com.flashcard.controller.usercardseen.request.UserSeenCardSaveRequest;
import com.flashcard.controller.usercardseen.response.UserCardSeenResponse;
import com.flashcard.model.UserSeenCard;
import com.flashcard.service.UserSeenCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-card-seen")
@RequiredArgsConstructor
public class UserCardSeenController {

    private final UserSeenCardService userCardSeenService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> save(@RequestBody @Valid UserSeenCardSaveRequest userCardSeenSaveRequest) {

        List<UserSeenCard> userSeenCard = userCardSeenService.save(userCardSeenSaveRequest);

        List<UserCardSeenResponse> response = userSeenCard.stream().map(UserCardSeenResponse::new).toList();

        return ResponseEntity.ok(response);
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
