package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.flashcard.admin.request.FlashcardSaveRequest;
import com.flashcard.controller.flashcard.admin.request.FlashcardUpdateRequest;
import com.flashcard.controller.flashcard.user.response.FlashcardUserResponse;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.*;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.TopicRepository;
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
    private final CardRepository cardRepository;
    private final FlashcardExcelImporter flashcardExcelImporter;
    private final UserSeenCardService userSeenCardService;
    private final UserCardPercentageService userCardPercentageService;
    private final AuthService authService;

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

        Flashcard flashcard = new Flashcard();
        flashcard.setTopic(topic);
        flashcard.setCardName(flashcardSaveRequest.getCardName());
        flashcard.setCanBePublish(false);
        flashcard.setIndex(flashcardSaveRequest.getIndex());

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

        return flashCardRepository.findByTopic(topic, pageable);
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

        // List<UserSeenCard> seenCards = userSeenCardService.getAllSeenCardsByTopic(topicId);
//
        // List<Long> flashcards = seenCards.stream()
        //         .map(UserSeenCard::getCard)
        //         .map(Card::getFlashcard)
        //         .map(Flashcard::getId)
        //         .distinct()
        //         .toList();

        List<Long> flashcardIds = userSeenCardService.findFlashcardIdsByTopic(user, topicId);

        return flashCardRepository.findByTopic(topic)
                .stream()
                .map(flashcard ->
                        new FlashcardUserResponse(
                                flashcard,
                                Math.toIntExact(flashcardLongMap.get(flashcard)),
                                flashcardIds.contains(flashcard.getId())))
                .toList();
    }

    //  @Cacheable(value = "flashcardSearch", key = "#search")
    public List<Flashcard> search(String search) {

        return flashCardRepository.search(search);
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

        flashcard.setCanBePublish(true);
        flashCardRepository.save(flashcard);

        userCardPercentageService.updateCardCount(flashcard.getTopic().getLesson());
    }
}
