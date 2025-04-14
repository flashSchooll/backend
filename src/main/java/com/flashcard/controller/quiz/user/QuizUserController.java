package com.flashcard.controller.quiz.user;

import com.flashcard.constants.Constants;
import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import com.flashcard.controller.quiz.request.QuizSaveRequest;
import com.flashcard.controller.quiz.request.UserQuizAnswerRequestList;
import com.flashcard.controller.quiz.response.*;
import com.flashcard.model.*;
import com.flashcard.repository.TopicRepository;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.FillBlankQuizService;
import com.flashcard.service.MyQuizService;
import com.flashcard.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quiz/user")
@RequiredArgsConstructor
public class QuizUserController {

    private final QuizService quizService;
    private final MyQuizService myQuizService;
    private final FillBlankQuizService fillBlankQuizService;
    private final AuthService authService;
    private final TopicRepository topicRepository;

    @GetMapping("/get-all/{topicId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAll(@PathVariable Long topicId) {

        List<QuizResponse> quizList = quizService.getByTopic(topicId);

        return ResponseEntity.ok(quizList);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getByName(@RequestParam String name, @RequestParam("topicId") Long topicId) {

        List<QuizResponse> responses = quizService.getByNameAndTopic(name, topicId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/quiz-count/{topicId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getQuizCount(@PathVariable Long topicId) {
        User user = authService.getCurrentUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));
        List<QuizCount> quizList = quizService.countByTopic(user, topic);

        List<FillBlankQuizUserResponse> fillBlankQuizUserResponses = fillBlankQuizService.getCountByUser(user, topic);

        List<QuizCountResponse> response = quizList.stream()
                .map(QuizCountResponse::new)
                .toList();
        List<QuizCountResponse> response1 = fillBlankQuizUserResponses.stream()
                .map(QuizCountResponse::new)
                .toList();

        List<QuizCountResponse> responses = new ArrayList<>();
        responses.addAll(response1);
        responses.addAll(response);

        responses.sort(Comparator.comparing(QuizCountResponse::getName));

        return ResponseEntity.ok(responses);
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

    @DeleteMapping("/my-quiz/{quizId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteQuiz(@PathVariable Long quizId) {

        myQuizService.deleteMyQuiz(quizId);

        return ResponseEntity.ok("Quiz başarıyla silindi");
    }

    @GetMapping("/my-quiz/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAllMyQuiz() {
        User user = authService.getCurrentUser();
        List<MyQuiz> myQuizs = myQuizService.myQuizes(user.getId());

        List<MyQuizResponse> responses = myQuizs.stream().map(MyQuizResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-answers")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getQuizAnswer(@RequestParam String name, @RequestParam Long topicId) {
        User user = authService.getCurrentUser();
        List<UserQuizAnswer> answerList = quizService.getAnswers(user.getId(), name, topicId);

        List<UserQuizAnswerResponse> responses = answerList.stream().map(UserQuizAnswerResponse::new).toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/repeat-quiz")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUserQuizAnswer(@RequestParam String name, @RequestParam Long topicId) {

        quizService.deleteUserQuiz(name, topicId);

        return ResponseEntity.ok("Quiz başarıyla silindi");
    }
}
