package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.flashcard.admin.request.FlashcardSaveRequest;
import com.flashcard.controller.flashcard.admin.request.FlashcardUpdateRequest;
import com.flashcard.controller.flashcard.user.response.FlashcardSearchResponse;
import com.flashcard.controller.flashcard.user.response.FlashcardUserResponse;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.*;
import com.flashcard.model.enums.YKS;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserSeenCardRepository;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.excel.FlashcardExcelImporter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashCardService {

    private final FlashCardRepository flashCardRepository;
    private final TopicRepository topicRepository;
    private final FlashcardExcelImporter flashcardExcelImporter;
    private final UserSeenCardService userSeenCardService;
    private final UserCardPercentageService userCardPercentageService;
    private final AuthService authService;
    private final UserSeenCardRepository userSeenCardRepository;

    @Transactional
    public Flashcard save(FlashcardSaveRequest flashcardSaveRequest) {
        Objects.requireNonNull(flashcardSaveRequest.getTopicId());
        Objects.requireNonNull(flashcardSaveRequest.getCardName());

        Topic topic = topicRepository.findById(flashcardSaveRequest.getTopicId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        boolean isExist = flashCardRepository.existsByCardName(flashcardSaveRequest.getCardName());

        if (isExist) {
            throw new BadRequestException(Constants.FLASHCARD_NAME_ALREADY_EXISTS);
        }
        Integer maxIndex = flashcardSaveRequest.getIndex();

        if (maxIndex == null) {
            maxIndex = flashCardRepository.findMaxIndexByTopic(topic) + 1;
        }

        Flashcard flashcard = new Flashcard();
        flashcard.setTopic(topic);
        flashcard.setCardName(flashcardSaveRequest.getCardName());
        flashcard.setCanBePublish(false);
        flashcard.setIndex(maxIndex);
        flashcard.setCardCount(0);
        flashcard.setDeleted(false);

        return flashCardRepository.save(flashcard);
    }

    @Transactional
    public Flashcard update(FlashcardUpdateRequest flashcardUpdateRequest) {
        Objects.requireNonNull(flashcardUpdateRequest.getFlashcardId());
        Objects.requireNonNull(flashcardUpdateRequest.getCardName());

        Flashcard flashcard = flashCardRepository.findById(flashcardUpdateRequest.getFlashcardId())
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        boolean isExist = flashCardRepository.existsByCardName(flashcardUpdateRequest.getCardName());

        if (isExist) {
            throw new BadRequestException(Constants.FLASHCARD_NAME_ALREADY_EXISTS);
        }

        flashcard.setCardName(flashcardUpdateRequest.getCardName());

        return flashCardRepository.save(flashcard);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        Flashcard flashcard = flashCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        flashCardRepository.delete(flashcard);
    }

    public Page<Flashcard> getAll(Long topicId, Pageable pageable) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        return flashCardRepository.findByTopicAndCanBePublishTrue(topic, pageable);
    }

    public Page<Flashcard> getAll(Pageable pageable) {

        return flashCardRepository.findAll(pageable);
    }

    public List<FlashcardUserResponse> getAllUser(Long topicId) {
        Objects.requireNonNull(topicId);
        User user = authService.getCurrentUser();

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));


        List<Object[]> result = flashCardRepository.findFlashcardsWithCountByTopic(topic);

        Map<Flashcard, Long> flashcardLongMap = new HashMap<>();

        for (Object[] row : result) {
            Flashcard flashcard = (Flashcard) row[0];
            Long count = (Long) row[1];
            flashcardLongMap.put(flashcard, count);
        }

        List<Long> flashcardIds = userSeenCardService.findFlashcardIdsByTopic(user, topicId);

        return flashCardRepository.findByTopicAndCanBePublishTrue(topic)
                .stream()
                .map(flashcard ->
                        new FlashcardUserResponse(
                                flashcard,
                                Math.toIntExact(flashcardLongMap.get(flashcard)),
                                flashcardIds.contains(flashcard.getId())))
                .toList();
    }

    //  @Cacheable(value = "flashcardSearch", key = "#search")
    public List<FlashcardSearchResponse> search(String search, YKS yks) {
        User user = authService.getCurrentUser();

        List<Flashcard> flashcards = Collections.emptyList();
        if (yks == null) {
            flashcards = flashCardRepository.search(search);
        } else {
            flashcards = flashCardRepository.findByTopicLessonYksAndSearch(yks, search);
        }

        List<Long> flashcardIds = userSeenCardRepository.findByUserWithAllData(user);

        return flashcards.stream()
                .map(flashcard -> new FlashcardSearchResponse(flashcard, flashcardIds.contains(flashcard.getId())))
                .toList();
    }

    @Transactional
    public void importExcel(Long lessonId, MultipartFile file) throws Exception {

        try {
            flashcardExcelImporter.saveExcel(lessonId, file);

        } catch (IOException e) {
            log.error("Ders eklenirken hata oldu : {}", lessonId);
            throw new IOException(e);
        }
    }

    @Transactional
    public void publish(Long flashcardId) {
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.FLASHCARD_NOT_FOUND));

        if (Boolean.FALSE.equals(flashcard.getCanBePublish())) {
            flashcard.setCanBePublish(true);
            flashCardRepository.save(flashcard);

            userCardPercentageService.updateCardCount(flashcard.getTopic().getLesson());

            Topic topic = flashcard.getTopic();
            topic.updateCardCount(flashcard.getCardCount());
        }
    }

    @Transactional
    public void publish(List<Long> flashcardIdList) {
        List<Flashcard> flashcardList = flashCardRepository.findByIdIn(flashcardIdList);

        for (Flashcard flashcard : flashcardList) {
            if (Boolean.FALSE.equals(flashcard.getCanBePublish())) {
                flashcard.setCanBePublish(true);
                flashCardRepository.save(flashcard);

                userCardPercentageService.updateCardCount(flashcard.getTopic().getLesson());
            }
        }
    }

    @Transactional
    public void publishAll() {
        List<Flashcard> flashcards = flashCardRepository.findByCanBePublishFalse();
        for (Flashcard flashcard : flashcards) {
            flashcard.setCanBePublish(true);
            flashCardRepository.save(flashcard);

            userCardPercentageService.updateCardCount(flashcard.getTopic().getLesson());
        }
    }
}
