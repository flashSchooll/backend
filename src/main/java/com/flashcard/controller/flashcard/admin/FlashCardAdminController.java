package com.flashcard.controller.flashcard.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.flashcard.admin.request.FlashcardSaveRequest;
import com.flashcard.controller.flashcard.admin.request.FlashcardUpdateRequest;
import com.flashcard.controller.flashcard.admin.response.FlashcardResponse;
import com.flashcard.model.Flashcard;
import com.flashcard.service.FlashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flashcard/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FlashCardAdminController {

    private final FlashCardService flashCardService;

    @PostMapping
    public ResponseEntity<FlashcardResponse> save(@RequestBody FlashcardSaveRequest tytFlashcardSaveRequest) {

        Flashcard response = flashCardService.save(tytFlashcardSaveRequest);

        FlashcardResponse flashcardResponse = new FlashcardResponse(response);

        return ResponseEntity.ok(flashcardResponse);
    }

    @PutMapping
    public ResponseEntity<FlashcardResponse> update(@RequestBody FlashcardUpdateRequest tytFlashcardUpdateRequest) {

        Flashcard response = flashCardService.update(tytFlashcardUpdateRequest);

        FlashcardResponse flashcardResponse = new FlashcardResponse(response);

        return ResponseEntity.ok(flashcardResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        flashCardService.delete(id);

        return ResponseEntity.ok(Constants.FLASHCARD_SUCCESSFULLY_DELETED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<FlashcardResponse>> getAll(@RequestParam(required = false) Long topicId,
                                                          @PageableDefault(sort = "cardName", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<FlashcardResponse> response;

        if (topicId == null) {
            response = flashCardService.getAll(pageable).map(FlashcardResponse::new);
        } else {
            response = flashCardService.getAll(topicId, pageable).map(FlashcardResponse::new);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/import-excel")
    public ResponseEntity<Object> importExcel(@RequestBody MultipartFile file,
                                              @RequestParam Long lessonId) throws Exception {

        flashCardService.importExcel(lessonId, file);

        return ResponseEntity.ok(Constants.EXCEL_SUCCESSFULLY_IMPORTED);
    }

    @PutMapping("/publish/{flashcardId}")
    public ResponseEntity<Object> publish(@PathVariable("flashcardId") Long flashcardId) {
        flashCardService.publish(flashcardId);

        return ResponseEntity.ok("Başarıyla yayınlandı");
    }
}
