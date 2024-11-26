package com.flashcard.controller.dailytarget.admin;

import com.flashcard.controller.dailytarget.response.DailyTargetAdminResponse;
import com.flashcard.controller.dailytarget.response.DailyTargetResponse;
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
    public ResponseEntity<?> getAll(@RequestParam(required = false) String search,
                                    @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DailyTargetAdminResponse> response = dailyTargetService.getAllAdmin(search, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPage(@RequestParam(required = false) String search) {
        List<DailyTargetAdminResponse> response = dailyTargetService.getAll(search);

        return ResponseEntity.ok(response);
    }
}
