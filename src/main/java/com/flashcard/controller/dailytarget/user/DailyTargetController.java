package com.flashcard.controller.dailytarget.user;

import com.flashcard.controller.dailytarget.response.DailyTargetPastResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetStatisticResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.DailyTargetService;
import lombok.RequiredArgsConstructor;
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
    public ResponseObject getTarget() {
        DailyTargetResponse response = dailyTargetService.getTarget();

        return ResponseObject.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseObject getTargetMonthly() {
        List<DailyTargetStatisticResponse> response = dailyTargetService.getTargetMonthly();

        return ResponseObject.ok(response);
    }

    @GetMapping("/past")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseObject getTargetPast() {
        List<DailyTargetPastResponse> response = dailyTargetService.getTargetPast();

        return ResponseObject.ok(response);
    }

}
