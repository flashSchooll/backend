package com.flashcard.controller.dailytarget.admin;

import com.flashcard.controller.dailytarget.response.DailyTargetAdminResponse;
import com.flashcard.model.DailyTarget;
import com.flashcard.service.DailyTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/daily-target/admin")
@RequiredArgsConstructor
public class DailyTargetAdminController {

    private final DailyTargetService dailyTargetService;

    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DailyTargetAdminResponse>> getAll(@RequestParam(required = false) String search,
                                    @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DailyTarget> dailyTargets = dailyTargetService.getAllAdmin(search, pageable);

        Page<DailyTargetAdminResponse> response = dailyTargets.map(DailyTargetAdminResponse::new);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DailyTargetAdminResponse>> getAllPage(@RequestParam(required = false) String search) {
        List<DailyTarget> dailyTargets = dailyTargetService.getAll(search);

        List<DailyTargetAdminResponse> response = dailyTargets.stream().map(DailyTargetAdminResponse::new).toList();

        return ResponseEntity.ok(response);
    }
}
