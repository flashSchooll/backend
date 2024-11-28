package com.flashcard.controller.usercardseen;

import com.flashcard.controller.usercardseen.request.UserCardSeenSaveRequest;
import com.flashcard.controller.usercardseen.response.UserCardSeenResponse;
import com.flashcard.model.UserSeenCard;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.UserCardSeenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseObject save(@RequestBody @Valid UserCardSeenSaveRequest userCardSeenSaveRequest) {

        List<UserSeenCard> userSeenCard = userCardSeenService.save(userCardSeenSaveRequest);

        List<UserCardSeenResponse> response = userSeenCard.stream().map(UserCardSeenResponse::new).toList();

        return ResponseObject.ok(response);
    }

    @GetMapping("/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseObject get(@PathVariable Long flashcardId) {

        List<UserSeenCard> response = userCardSeenService.getAllSeenCardsByFlashcard(flashcardId);

        List<UserCardSeenResponse> responseList = response.stream().map(UserCardSeenResponse::new).toList();

        return ResponseObject.ok(responseList);
    }

    @GetMapping("/unknown/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseObject getUnknown(@PathVariable Long flashcardId) {

        List<UserSeenCard> response = userCardSeenService.getUnknownSeenCardsByFlashcard(flashcardId);

        List<UserCardSeenResponse> responseList = response.stream().map(UserCardSeenResponse::new).toList();

        return ResponseObject.ok(responseList);
    }

    @GetMapping("/known/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseObject getKnown(@PathVariable Long flashcardId) {

        List<UserSeenCard> response = userCardSeenService.getKnownSeenCardsByFlashcard(flashcardId);

        List<UserCardSeenResponse> responseList = response.stream().map(UserCardSeenResponse::new).toList();

        return ResponseObject.ok(responseList);
    }

}
