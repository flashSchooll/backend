package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.quiz.request.UpdateQuizRequest;
import com.flashcard.controller.quiz.request.UserQuizAnswerRequest;
import com.flashcard.controller.quiz.request.UserQuizAnswerRequestList;
import com.flashcard.controller.quiz.response.QuizCount;
import com.flashcard.controller.quiz.response.QuizResponse;
import com.flashcard.model.*;
import com.flashcard.model.enums.QuizOption;
import com.flashcard.model.enums.QuizType;
import com.flashcard.repository.*;
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
    private final UserRepository userRepository;
    private final MyQuizRepository myQuizRepository;


    @Transactional
    public void importExcel(Long topicId, MultipartFile file) throws Exception {


        try {
            quizExcelImporter.saveExcel(topicId, file);

        } catch (IOException e) {
            log.error("Quiz eklenirken hata oldu : {}", topicId);
            throw new Exception(e);
        }

    }

    public List<QuizResponse> getByTopic(Long topicId) {
        Objects.requireNonNull(topicId);
        User user = authService.getCurrentUser();
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<MyQuiz> myQuizs = myQuizRepository.findByUserAndQuizTopicWithFetch(user, topic);
        Map<Long, Quiz> quizMap = myQuizs.stream()
                .collect(Collectors.toMap(
                        myQuiz -> myQuiz.getQuiz().getId(),  // Quiz'in ID'sini key olarak alıyoruz
                        MyQuiz::getQuiz  // Quiz nesnesini value olarak alıyoruz
                ));

        List<Quiz> quizList = quizRepository.findByTopic(topic);

        return quizList.stream().map(quiz -> new QuizResponse(quiz, quizMap.get(quiz.getId()) != null)).toList();
    }

    public List<QuizResponse> getByName(String name) {
        Objects.requireNonNull(name);
        User user = authService.getCurrentUser();

        List<MyQuiz> myQuizs = myQuizRepository.findByUserAndQuizNameWithFetch(user, name);

        Map<Long, Quiz> quizMap = myQuizs.stream()
                .collect(Collectors.toMap(
                        myQuiz -> myQuiz.getQuiz().getId(),  // Quiz'in ID'sini key olarak alıyoruz
                        MyQuiz::getQuiz  // Quiz nesnesini value olarak alıyoruz
                ));

        List<Quiz> quizList = quizRepository.findByName(name);

        return quizList.stream().map(quiz -> new QuizResponse(quiz, quizMap.get(quiz.getId()) != null)).toList();

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

    //  @Cacheable(value = "quizCounts", key = "{#userId,#topicId}")
    public List<QuizCount> countByTopic(Long userId, Long topicId) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<Quiz> quizList = quizRepository.findByTopic(topic);

        Map<String, Long> map = quizList.stream().collect(Collectors.groupingBy(
                Quiz::getName,
                Collectors.counting()
        ));

        Map<String, List<Quiz>> listMap = quizList.stream().collect(Collectors.groupingBy(Quiz::getName));

        List<QuizCount> quizCounts = new ArrayList<>();
        QuizCount quizCount;
        for (Map.Entry<String, Long> entry : map.entrySet()) {

            boolean existQuiz = userQuizAnswerRepository.existsByUserIdAndQuizName(userId, entry.getKey());
            QuizType type = listMap.get(entry.getKey()).get(0).getType();
            quizCount = new QuizCount(entry.getKey(), entry.getValue(), topicId, existQuiz, type);//type eklenecek

            quizCounts.add(quizCount);
        }

        return quizCounts;
    }

    public void saveAnswer(UserQuizAnswerRequestList userQuizAnswerRequest) {
        User user = authService.getCurrentUser();

        List<Quiz> quizList = quizRepository.findByName(userQuizAnswerRequest.getName());
        Map<Long, Quiz> quizMap = quizList.stream()
                .collect(Collectors.toMap(Quiz::getId, quiz -> quiz));


        int quizCount = quizRepository.countByName(userQuizAnswerRequest.getName());

        if (userQuizAnswerRequest.getAnswerList().size() != quizCount) {
            throw new IllegalArgumentException("Quizdeki soru sayısı doğru değil");
        }

        List<UserQuizAnswer> answerList = new ArrayList<>();

        UserQuizAnswer answer;
        int starCount = 0;

        for (UserQuizAnswerRequest u : userQuizAnswerRequest.getAnswerList()) {
            answer = new UserQuizAnswer();
            answer.setUser(user);
            answer.setAnswer(QuizOption.byIndex(u.getIndex()));
            answer.setQuiz(quizMap.get(u.getQuizId()));

            if (u.getIndex().equals(quizMap.get(u.getQuizId()).getAnswer().getIndex())) {
                starCount++;
            }

            answerList.add(answer);
        }

        userQuizAnswerRepository.saveAll(answerList);

        user.raiseStar(starCount);
        user.raiseRosette();

        userRepository.save(user);

    }

    //  @Cacheable(value = "userQuizAnswers", key = "{#userId,#name}")
    public List<UserQuizAnswer> getAnswers(Long userId, String name) {

        return userQuizAnswerRepository.findByUserIdAndQuizName(userId, name);
    }

    public List<QuizResponse> getAllByName(String name) {
        Objects.requireNonNull(name);

        return quizRepository.findByName(name).stream().map(quiz -> new QuizResponse(quiz, false)).toList();
    }
}