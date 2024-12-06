package com.flashcard.controller.lesson.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.lesson.admin.response.LessonResponse;
import com.flashcard.model.Lesson;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import com.flashcard.service.LessonService;
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
@RequestMapping("/api/lesson/admin")
@RequiredArgsConstructor
public class LessonAdminController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestParam YKSLesson yksLesson,
                                  @RequestParam Branch branch,
                                  @RequestParam YKS yks,
                                  @RequestBody MultipartFile icon) throws IOException {

        Lesson lesson = lessonService.save(yks, branch, yksLesson, icon);

        LessonResponse response = new LessonResponse(lesson);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestParam Long id,
                                    @RequestParam YKSLesson yksLesson,
                                    @RequestParam Branch branch,
                                    @RequestParam YKS yks,
                                    @RequestBody MultipartFile icon) throws IOException {

        Lesson lesson = lessonService.update(id, yksLesson, branch, yks, icon);

        LessonResponse response = new LessonResponse(lesson);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        lessonService.delete(id);

        return ResponseEntity.ok(Constants.LESSON_SUCCESSFULLY_DELETED);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getId() {

        List<Lesson> lessons = lessonService.getAll();

        List<LessonResponse> response = lessons.stream().map(LessonResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEnums() {

        List<String> response = Arrays.stream(YKSLesson.values()).map(Enum::name).toList();

        return ResponseEntity.ok(response);
    }



}