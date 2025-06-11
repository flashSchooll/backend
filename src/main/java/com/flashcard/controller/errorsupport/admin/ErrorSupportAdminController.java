package com.flashcard.controller.errorsupport.admin;

import com.flashcard.controller.errorsupport.admin.response.ErrorSupportResponse;
import com.flashcard.model.ErrorSupport;
import com.flashcard.service.ErrorSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/error-support/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ErrorSupportAdminController {
    private final ErrorSupportService errorSupportService;

    @GetMapping("/get-all-as-page")
    public ResponseEntity<Page<ErrorSupportResponse>> getAllErrorSupport(@RequestParam(required = false) Boolean isSolved,
                                                                         @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ErrorSupport> errorSupports = errorSupportService.getAll(isSolved, pageable);
        Page<ErrorSupportResponse> errorSupportResponses = errorSupports.map(ErrorSupportResponse::new);
        return ResponseEntity.ok(errorSupportResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ErrorSupportResponse> get(@PathVariable Long id) {
        ErrorSupport errorSupport = errorSupportService.get(id);
        ErrorSupportResponse response = new ErrorSupportResponse(errorSupport);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ErrorSupportResponse> solve(@PathVariable Long id) {
        ErrorSupport errorSupport = errorSupportService.solve(id);
        ErrorSupportResponse response = new ErrorSupportResponse(errorSupport);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        errorSupportService.delete(id);
        return ResponseEntity.ok().build();
    }

}
