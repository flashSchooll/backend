package com.flashcard.controller.tytflashcard.user;

import com.flashcard.controller.tytflashcard.user.response.TYTFlashcardUserResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTFlashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/flashcard/user")
@RequiredArgsConstructor
public class TYTFlashcardUserController {

    private final TYTFlashCardService tytFlashCardService;

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseObject getAll(@PathVariable Long topicId) {

        List<TYTFlashcardUserResponse> response = tytFlashCardService.getAllUser(topicId);

        return ResponseObject.ok(response);
    }


}
