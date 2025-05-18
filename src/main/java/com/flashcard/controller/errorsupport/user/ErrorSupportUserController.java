package com.flashcard.controller.errorsupport.user;

import com.flashcard.controller.errorsupport.user.request.ErrorSupportSaveRequest;
import com.flashcard.model.ErrorSupport;
import com.flashcard.service.ErrorSupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/error-support/user")
@RequiredArgsConstructor
public class ErrorSupportUserController {
    private final ErrorSupportService errorSupportService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> saveErrorSupport(@RequestBody @Valid ErrorSupportSaveRequest errorSupport) {
        errorSupportService.createErrorSupport(errorSupport);
        return ResponseEntity.ok().build();
    }
}
