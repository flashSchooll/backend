package com.flashcard.controller.userpdfurl;

import com.flashcard.controller.growingreport.response.GrowingReportResponse;
import com.flashcard.controller.userpdfurl.request.UserPdfUrlRequest;
import com.flashcard.controller.userpdfurl.response.UserPdfUrlResponse;
import com.flashcard.service.UserPdfUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-pdf")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserPdfUrlController {
    private UserPdfUrlService userPdfUrlService;


    @PostMapping
    public ResponseEntity<String> saveUrl(@RequestBody UserPdfUrlRequest request) {
        userPdfUrlService.save(request);

        return ResponseEntity.ok("Successfull");
    }

    @GetMapping
    public ResponseEntity<List<UserPdfUrlResponse>> getUsersPdfUrls() {
        List<UserPdfUrlResponse> response = userPdfUrlService.getUserUrls();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUrl(@PathVariable UUID id){
        userPdfUrlService.deleteById(id);

        return ResponseEntity.ok("Deleted successfull");
    }
}
