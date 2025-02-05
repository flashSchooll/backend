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
import com.flashcard.model.enums.QuizOption;
import com.flashcard.repository.QuizRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserQuizAnswerRepository;
import com.flashcard.repository.UserRepository;
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
        User user=authService.getCurrentUser();

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

            boolean existQuiz=userQuizAnswerRepository.existsByUserAndQuizName(user,entry.getKey());
            quizCount = new QuizCount(entry.getKey(), entry.getValue(), topicId,existQuiz);

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

    public List<UserQuizAnswer> getAnswers(String name) {
        User user = authService.getCurrentUser();

        return userQuizAnswerRepository.findByUserAndQuizName(user, name);
    }
}