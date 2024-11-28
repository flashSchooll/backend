package com.flashcard.controller.tytcard.admin;

import com.flashcard.controller.tytcard.admin.request.TYTCardSaveAllRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardSaveRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardUpdateRequest;
import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/card/admin")
@RequiredArgsConstructor
public class TYTCardAdminController {

    private final TYTCardService tytCardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject save(@RequestBody @Valid TYTCardSaveRequest tytCardSaveRequest) throws IOException {

        TYTCardResponse response = tytCardService.save(tytCardSaveRequest);

        return ResponseObject.ok(response);
    }

    @PostMapping("/create-all/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject saveAll(@RequestBody @Valid TYTCardSaveAllRequest request,
                                     @PathVariable Long flashcardId) throws IOException {

        List<TYTCardResponse> response = tytCardService.saveAll(flashcardId,request);

        return ResponseObject.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject update(@RequestBody TYTCardUpdateRequest tytCardUpdateRequest) throws IOException {

        TYTCardResponse response = tytCardService.update(tytCardUpdateRequest);

        return ResponseObject.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject delete(@PathVariable Long id) {

        tytCardService.delete(id);

        return ResponseObject.ok("Card başarıyla silindi");
    }

    @GetMapping("/get-all/{flashcardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAll(@PathVariable Long flashcardId) {

        List<TYTCardResponse> response = tytCardService.getAll(flashcardId);

        return ResponseObject.ok(response);
    }
}
