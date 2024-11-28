package com.flashcard.controller.tytflashcard.admin;


import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardSaveRequest;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardUpdateRequest;
import com.flashcard.controller.tytflashcard.admin.response.TYTFlashcardResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTFlashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/flashcard/admin")
@RequiredArgsConstructor
public class TYTFlashCardAdminController {

    private final TYTFlashCardService tytFlashCardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject save(@RequestBody TYTFlashcardSaveRequest tytFlashcardSaveRequest) {

        TYTFlashcardResponse response = tytFlashCardService.save(tytFlashcardSaveRequest);

        return ResponseObject.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject update(@RequestBody TYTFlashcardUpdateRequest tytFlashcardUpdateRequest) {

        TYTFlashcardResponse response = tytFlashCardService.update(tytFlashcardUpdateRequest);

        return ResponseObject.ok(response);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject delete(@PathVariable Long id) {

        tytFlashCardService.delete(id);

        return ResponseObject.ok("Flashcard başarıyla silindi");
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAll(@PathVariable Long topicId) {

        List<TYTFlashcardResponse> response = tytFlashCardService.getAll(topicId)
                .stream().map(TYTFlashcardResponse::new).toList();

        return ResponseObject.ok(response);
    }
}
