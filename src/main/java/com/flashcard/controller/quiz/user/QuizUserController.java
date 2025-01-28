package com.flashcard.controller.quiz.user;

import com.flashcard.controller.quiz.request.QuizSaveRequest;
import com.flashcard.controller.quiz.request.UserQuizAnswerRequestList;
import com.flashcard.controller.quiz.response.MyQuizResponse;
import com.flashcard.controller.quiz.response.QuizCount;
import com.flashcard.controller.quiz.response.QuizResponse;
import com.flashcard.controller.quiz.response.UserQuizAnswerResponse;
import com.flashcard.model.MyQuiz;
import com.flashcard.model.Quiz;
import com.flashcard.model.UserQuizAnswer;
import com.flashcard.service.MyQuizService;
import com.flashcard.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quiz/user")
@RequiredArgsConstructor
public class QuizUserController {

    private final QuizService quizService;
    private final MyQuizService myQuizService;

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAll(@PathVariable Long topicId) {

        List<Quiz> quizList = quizService.getByTopic(topicId);

        List<QuizResponse> responses = quizList.stream().map(QuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getByName(@RequestParam String name) {

        List<Quiz> quizList = quizService.getByName(name);

        List<QuizResponse> responses = quizList.stream().map(QuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quiz-count/{topicId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getQuizCount(@PathVariable Long topicId) {

        List<QuizCount> quizList = quizService.countByTopic(topicId);

        return ResponseEntity.ok(quizList);
    }

    @PostMapping("/save-answer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> saveQuizAnswer(@RequestBody UserQuizAnswerRequestList userQuizAnswerRequest) {

        quizService.saveAnswer(userQuizAnswerRequest);

        return ResponseEntity.ok("Cevaplar başarıyla kaydedildi");
    }


    @PostMapping("/my-quiz")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> saveQuiz(@RequestBody QuizSaveRequest quizSaveRequest) {

        myQuizService.saveMyQuiz(quizSaveRequest);

        return ResponseEntity.ok("Quiz başarıyla kaydedildi");
    }

    @DeleteMapping("/my-quiz/{myQuizId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteQuiz(@PathVariable Long myQuizId) {

        myQuizService.deleteMyQuiz(myQuizId);

        return ResponseEntity.ok("Quiz başarıyla silindi");
    }

    @GetMapping("/my-quiz/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAllMyQuiz() {

        List<MyQuiz> myQuizs = myQuizService.myQuizes();

        List<MyQuizResponse> responses = myQuizs.stream().map(MyQuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-answers")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getQuizAnswer(@RequestParam String name) {

        List<UserQuizAnswer> answerList = quizService.getAnswers(name);

        List<UserQuizAnswerResponse> responses = answerList.stream().map(UserQuizAnswerResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

}
