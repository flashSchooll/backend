package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.quiz.request.UpdateQuizRequest;
import com.flashcard.controller.quiz.request.UserQuizAnswerRequest;
import com.flashcard.controller.quiz.request.UserQuizAnswerRequestList;
import com.flashcard.controller.quiz.response.QuizCount;
import com.flashcard.model.Quiz;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserQuizAnswer;
import com.flashcard.repository.QuizRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserQuizAnswerRepository;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.excel.QuizExcelImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizExcelImporter quizExcelImporter;
    private final TopicRepository topicRepository;
    private final AuthService authService;
    private final UserQuizAnswerRepository userQuizAnswerRepository;


    @Transactional
    public void importExcel(Long topicId, MultipartFile file) throws Exception {


        try {
            quizExcelImporter.saveExcel(topicId, file);

        } catch (IOException e) {
            log.error("Quiz eklenirken hata oldu : {}", topicId);
            throw new Exception(e);
        }

    }

    public List<Quiz> getByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        return quizRepository.findByTopic(topic);
    }

    public List<Quiz> getByName(String name) {
        Objects.requireNonNull(name);

        return quizRepository.findByName(name);
    }

    @Transactional
    public Quiz updateQuiz(UpdateQuizRequest updateQuizRequest) {
        Objects.requireNonNull(updateQuizRequest.getId());

        Quiz quiz = quizRepository.findById(updateQuizRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("Quiz bulunamadı"));

        quiz.setName(updateQuizRequest.getName());
        quiz.setA(updateQuizRequest.getA());
        quiz.setB(updateQuizRequest.getB());
        quiz.setC(updateQuizRequest.getC());
        quiz.setD(updateQuizRequest.getD());
        quiz.setAnswer(updateQuizRequest.getAnswer());
        quiz.setName(updateQuizRequest.getName());

        return quizRepository.save(quiz);
    }

    public Page<Quiz> getAll(Pageable pageable) {

        return quizRepository.findAll(pageable);
    }

    public List<QuizCount> countByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<Quiz> quizList = quizRepository.findByTopic(topic);

        Map<String, Long> map = quizList.stream().collect(Collectors.groupingBy(
                Quiz::getName,
                Collectors.counting()
        ));

        List<QuizCount> quizCounts = new ArrayList<>();
        QuizCount quizCount;
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            quizCount = new QuizCount(entry.getKey(), entry.getValue(), topicId);

            quizCounts.add(quizCount);
        }

        return quizCounts;
    }

    public void saveAnswer(UserQuizAnswerRequestList userQuizAnswerRequest) {
        User user = authService.getCurrentUser();

        List<Quiz> quizList = quizRepository.findByName(userQuizAnswerRequest.getName());

        List<UserQuizAnswer> answerList = new ArrayList<>();

        UserQuizAnswer answer;

        for (UserQuizAnswerRequest u : userQuizAnswerRequest.getAnswerList()) {
            answer = new UserQuizAnswer();
            answer.setUser(user);
            answer.setAnswer(u.getAnswer());
            answer.setQuiz(quizList.stream().filter(f -> Objects.equals(u.getQuizId(), f.getId())).findFirst().orElseThrow(() -> new NoSuchElementException("quiz bulunamadı")));

            answerList.add(answer);
        }

        userQuizAnswerRepository.saveAll(answerList);

    }

    public List<UserQuizAnswer> getAnswers(String name) {
        User user = authService.getCurrentUser();

        return userQuizAnswerRepository.findByUserAndQuizName(user, name);
    }
}