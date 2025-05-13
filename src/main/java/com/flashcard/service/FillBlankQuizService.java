package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.fillblankquiz.user.response.FillBlankQuizUserResponse;
import com.flashcard.model.FillBlankQuiz;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.enums.QuizType;
import com.flashcard.repository.FillBlankQuizRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserFillBlankQuizRepository;
import com.flashcard.service.excel.FillBlankQuizExcelImporter;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class FillBlankQuizService {

    private final FillBlankQuizRepository fillBlankQuizRepository;
    private final FillBlankQuizExcelImporter fillBlankQuizExcelImporter;
    private final TopicRepository topicRepository;
    private final UserFillBlankQuizRepository userFillBlankQuizRepository;

    @Transactional
    public void importExcel(Long topicId, MultipartFile file) throws Exception {

        try {
            fillBlankQuizExcelImporter.saveExcel(topicId, file);
        } catch (IOException e) {
            log.error("Quiz eklenirken hata oldu : {}", topicId);
            throw new IOException(e);
        }
    }

    //   @Cacheable(value = "fillBlankQuizes", key = "#topicId")
    public Page<FillBlankQuiz> getByTopic(Long topicId, Pageable pageable) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        return fillBlankQuizRepository.findByTopicAsPage(pageable, topic);
    }

    @Transactional
    public void deleteById(Long id) {
        Objects.requireNonNull(id);

        FillBlankQuiz fillBlankQuiz = fillBlankQuizRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Boşluk doldurmalı soru bulunamadı"));

        fillBlankQuizRepository.delete(fillBlankQuiz);
    }

    //  @Cacheable(value = "fillBlankQuizesByTitle", key = "#title")
    public List<FillBlankQuiz> getByTitleAndTopic(String title, long topicId) {

        Objects.requireNonNull(title);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        return fillBlankQuizRepository.findByTitleAndTopic(title, topic);
    }

    public List<FillBlankQuizUserResponse> getCountByUser(User user, Topic topic) {

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
            response.setTopicId(topic.getId());
            response.setQuizType(QuizType.FILL_BLANK);

            responseList.add(response);
        }

        return responseList;
    }
}
