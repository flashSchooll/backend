package com.flashcard.controller.flashcard.admin;


import com.flashcard.constants.Constants;
import com.flashcard.controller.flashcard.admin.request.FlashcardSaveRequest;
import com.flashcard.controller.flashcard.admin.request.FlashcardUpdateRequest;
import com.flashcard.controller.flashcard.admin.response.FlashcardResponse;
import com.flashcard.model.Flashcard;
import com.flashcard.service.FlashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flashcard/admin")
@RequiredArgsConstructor
public class FlashCardAdminController {

    private final FlashCardService flashCardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody FlashcardSaveRequest tytFlashcardSaveRequest) {

        Flashcard response = flashCardService.save(tytFlashcardSaveRequest);

        FlashcardResponse flashcardResponse = new FlashcardResponse(response);

        return ResponseEntity.ok(flashcardResponse);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody FlashcardUpdateRequest tytFlashcardUpdateRequest) {

        Flashcard response = flashCardService.update(tytFlashcardUpdateRequest);

        FlashcardResponse flashcardResponse = new FlashcardResponse(response);

        return ResponseEntity.ok(flashcardResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        flashCardService.delete(id);

        return ResponseEntity.ok(Constants.FLASHCARD_SUCCESSFULLY_DELETED);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<FlashcardResponse> response = flashCardService.getAll(topicId)
                .stream().map(FlashcardResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importExcel(@RequestBody MultipartFile file,
                                         @RequestParam Long lessonId) throws IOException {

        flashCardService.importExcel(lessonId, file);

        return ResponseEntity.ok(Constants.EXCEL_SUCCESSFULLY_IMPORTED);
    }
}
