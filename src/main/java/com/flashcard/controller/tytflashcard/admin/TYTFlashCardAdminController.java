package com.flashcard.controller.tytflashcard.admin;


import com.flashcard.constants.Constants;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardSaveRequest;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardUpdateRequest;
import com.flashcard.controller.tytflashcard.admin.response.TYTFlashcardResponse;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.enums.TYT;
import com.flashcard.service.TYTFlashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/flashcard/admin")
@RequiredArgsConstructor
public class TYTFlashCardAdminController {

    private final TYTFlashCardService tytFlashCardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody TYTFlashcardSaveRequest tytFlashcardSaveRequest) {

        TYTFlashcard response = tytFlashCardService.save(tytFlashcardSaveRequest);

        TYTFlashcardResponse flashcardResponse = new TYTFlashcardResponse(response);

        return ResponseEntity.ok(flashcardResponse);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody TYTFlashcardUpdateRequest tytFlashcardUpdateRequest) {

        TYTFlashcard response = tytFlashCardService.update(tytFlashcardUpdateRequest);

        TYTFlashcardResponse flashcardResponse = new TYTFlashcardResponse(response);

        return ResponseEntity.ok(flashcardResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytFlashCardService.delete(id);

        return ResponseEntity.ok(Constants.FLASHCARD_SUCCESSFULLY_DELETED);
    }

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@PathVariable Long topicId) {

        List<TYTFlashcardResponse> response = tytFlashCardService.getAll(topicId)
                .stream().map(TYTFlashcardResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importExcel(@RequestBody MultipartFile file,
                                         @RequestParam Long tytLessonId) throws IOException {

        tytFlashCardService.importExcel(tytLessonId, file);

        return ResponseEntity.ok(Constants.EXCEL_SUCCESSFULLY_IMPORTED);
    }
}
