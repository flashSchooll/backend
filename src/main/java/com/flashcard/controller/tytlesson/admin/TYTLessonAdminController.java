package com.flashcard.controller.tytlesson.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tytlesson.admin.response.TYTLessonResponse;
import com.flashcard.model.TYTLesson;
import com.flashcard.model.enums.TYT;
import com.flashcard.service.TYTLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> save(@RequestParam TYT tyt,
                                  @RequestBody MultipartFile icon) throws IOException {

        TYTLesson tytLesson = tytLessonService.save(tyt, icon);

        TYTLessonResponse response = new TYTLessonResponse(tytLesson);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        tytLessonService.delete(id);

        return ResponseEntity.ok(Constants.LESSON_SUCCESSFULLY_DELETED);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getId() {

        List<TYTLesson> tytLessons = tytLessonService.getAll();

        List<TYTLessonResponse> response = tytLessons.stream().map(TYTLessonResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tyt")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEnums() {

        List<String> response = Arrays.stream(TYT.values()).map(Enum::name).toList();

        return ResponseEntity.ok(response);
    }


}