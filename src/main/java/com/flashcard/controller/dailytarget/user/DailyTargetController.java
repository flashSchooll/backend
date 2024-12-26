package com.flashcard.controller.dailytarget.user;

import com.flashcard.controller.dailytarget.response.DailyTargetPastResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetStatisticResponse;
import com.flashcard.model.DailyTarget;
import com.flashcard.model.MonthDailyTarget;
import com.flashcard.service.DailyTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/daily-target")
@RequiredArgsConstructor
public class DailyTargetController {

    private final DailyTargetService dailyTargetService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DailyTargetResponse> getTarget() {
        DailyTarget dailyTarget = dailyTargetService.getTarget();

        DailyTargetResponse response = new DailyTargetResponse(dailyTarget);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<DailyTargetStatisticResponse>> getTargetMonthly() {
        List<DailyTarget> dailyTargets = dailyTargetService.getTargetMonthly();

        List<DailyTargetStatisticResponse> response = dailyTargets.stream().map(DailyTargetStatisticResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/past")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<DailyTargetPastResponse>> getTargetPast() {
        List<MonthDailyTarget> response = dailyTargetService.getTargetPast();

        List<DailyTargetPastResponse> responseList = response.stream().map(DailyTargetPastResponse::new).toList();

        return ResponseEntity.ok(responseList);
    }


}
