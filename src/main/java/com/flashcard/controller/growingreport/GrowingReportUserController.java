package com.flashcard.controller.growingreport;

import com.flashcard.controller.growingreport.request.GrowingReportRequest;
import com.flashcard.controller.growingreport.response.GrowingReportResponse;
import com.flashcard.service.GrowingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/growing-report")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class GrowingReportUserController {

    private final GrowingReportService growingReportService;


    @GetMapping()
    public ResponseEntity<GrowingReportResponse> getUserReport(@RequestBody GrowingReportRequest request) {
        GrowingReportResponse response = growingReportService.getReportByUser(request);

        return ResponseEntity.ok(response);
    }
}
