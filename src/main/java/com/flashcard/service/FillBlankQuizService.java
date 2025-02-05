package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import com.flashcard.model.FillBlankQuiz;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.repository.FillBlankQuizRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserFillBlankQuizRepository;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.excel.FillBlankQuizExcelImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FillBlankQuizService {

    private final FillBlankQuizRepository fillBlankQuizRepository;
    private final FillBlankQuizExcelImporter fillBlankQuizExcelImporter;
    private final TopicRepository topicRepository;
    private final AuthService authService;
    private final UserFillBlankQuizRepository userFillBlankQuizRepository;

    @Transactional
    public void importExcel(Long topicId, MultipartFile file) throws Exception {

        try {
            fillBlankQuizExcelImporter.saveExcel(topicId, file);
        } catch (IOException e) {
            log.error("Quiz eklenirken hata oldu : {}", topicId);
            throw new Exception(e);
        }
    }

    public List<FillBlankQuiz> getByTopic(Long topicId) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        return fillBlankQuizRepository.findByTopic(topic);
    }

    @Transactional
    public void deleteById(Long id) {
        Objects.requireNonNull(id);

        FillBlankQuiz fillBlankQuiz = fillBlankQuizRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Boşluk doldurmalı soru bulunamadı"));

        fillBlankQuizRepository.delete(fillBlankQuiz);
    }

    public List<FillBlankQuiz> getByTitle(String title) {

        Objects.requireNonNull(title);

        return fillBlankQuizRepository.findByTitle(title);
    }

    public List<FillBlankQuizUserResponse> getCountByUser(Long topicId) {

        User user = authService.getCurrentUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<FillBlankQuiz> quizs = fillBlankQuizRepository.findByTopic(topic);
        Map<String, Long> map = quizs.stream()
                .collect(
                        Collectors.groupingBy(
                                FillBlankQuiz::getTitle,
                                Collectors.counting()));

        List<FillBlankQuizUserResponse> responseList = new ArrayList<>();
        FillBlankQuizUserResponse response;
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            String title = entry.getKey();
            Long count = entry.getValue();
            boolean seen = userFillBlankQuizRepository.countByUserAndTitle(user, title) != 0;

            response = new FillBlankQuizUserResponse();
            response.setCount(count);
            response.setTitle(title);
            response.setSeen(seen);
            response.setTopicId(topicId);

            responseList.add(response);
        }

        return responseList;
    }
}
