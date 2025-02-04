package com.flashcard.controller.fillblankquiz.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.fillblankquiz.admin.response.FillBlankQuizResponse;
import com.flashcard.model.FillBlankQuiz;
import com.flashcard.service.FillBlankQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fill-blank-quiz/admin")
@RequiredArgsConstructor
public class FillBlankQuizAdminController {

    private final FillBlankQuizService fillBlankQuizService;


    @PostMapping("/import-excel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> importExcel(@RequestBody MultipartFile file,
                                              @RequestParam Long topicId) throws Exception {

        fillBlankQuizService.importExcel(topicId, file);

        return ResponseEntity.ok(Constants.EXCEL_SUCCESSFULLY_IMPORTED);
    }

    @GetMapping("/{topicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getByTopic(@PathVariable Long topicId) {// todo burası page yapısıyla dönmeli ve bütün datayı dönmeli

        List<FillBlankQuiz> fillBlankQuizList = fillBlankQuizService.getByTopic(topicId);

        List<FillBlankQuizResponse> responses = fillBlankQuizList.stream().map(FillBlankQuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        fillBlankQuizService.deleteById(id);

        return ResponseEntity.ok("Quiz başarıyla silindi");
    }


}
