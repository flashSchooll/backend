package com.flashcard.controller.tytlesson.admin;

import com.flashcard.controller.tytlesson.admin.request.TYTLessonSaveRequest;
import com.flashcard.controller.tytlesson.admin.response.TYTLessonResponse;
import com.flashcard.model.enums.TYT;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.TYTLessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/lesson/admin")
@RequiredArgsConstructor
public class TYTLessonAdminController {

    private final TYTLessonService tytLessonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject save(@RequestBody @Valid TYTLessonSaveRequest tytLessonSaveRequest) throws IOException {

        tytLessonService.save(tytLessonSaveRequest);

        return ResponseObject.ok("Ders başarıyla kaydedildi");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject delete(@PathVariable Long id) {

        tytLessonService.delete(id);

        return ResponseObject.ok("Ders başarıyla silindi");
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getId() {

        List<TYTLessonResponse> response = tytLessonService.getAll();

        return ResponseObject.ok(response);
    }

    @GetMapping("/tyt")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getEnums() {

        List<String> response = Arrays.stream(TYT.values()).map(Enum::name).toList();

        return ResponseObject.ok(response);
    }


}