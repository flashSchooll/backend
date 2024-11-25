package com.flashcard.controller.dailytarget.user;

import com.flashcard.controller.dailytarget.response.DailyTargetAdminResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetPastResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetStatisticResponse;
import com.flashcard.service.DailyTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/daily-target")
@RequiredArgsConstructor
public class DailyTargetController {

    private final DailyTargetService dailyTargetService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTarget() {
        DailyTargetResponse response = dailyTargetService.getTarget();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTargetMonthly() {
        List<DailyTargetStatisticResponse> response = dailyTargetService.getTargetMonthly();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/past")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTargetPast() {
        List<DailyTargetPastResponse> response = dailyTargetService.getTargetPast();

        return ResponseEntity.ok(response);
    }

}
