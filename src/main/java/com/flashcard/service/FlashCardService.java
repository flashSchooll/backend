package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.flashcard.admin.request.FlashcardSaveRequest;
import com.flashcard.controller.flashcard.admin.request.FlashcardUpdateRequest;
import com.flashcard.controller.flashcard.user.response.FlashcardUserResponse;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.Card;
import com.flashcard.model.Flashcard;
import com.flashcard.model.Topic;
import com.flashcard.model.UserSeenCard;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.service.excel.FlashcardExcelImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashCardService {

    private final FlashCardRepository flashCardRepository;
    private final TopicRepository topicRepository;
    private final CardRepository cardRepository;
    private final FlashcardExcelImporter flashcardExcelImporter;
    private final UserSeenCardService userSeenCardService;

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

    public Page<Flashcard> getAll(Long topicId,Pageable pageable) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        return flashCardRepository.findByTopic(topic,pageable);
    }

    public Page<Flashcard> getAll(Pageable pageable) {

        return flashCardRepository.findAll(pageable);
    }

    public List<FlashcardUserResponse> getAllUser(Long topicId) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<Card> tytCards = cardRepository.findByTopic(topic);

        Map<Flashcard, Long> cardCount = tytCards.stream()
                .collect(Collectors.groupingBy(
                        Card::getFlashcard,
                        Collectors.counting()
                ));

        List<UserSeenCard> seenCards = userSeenCardService.getAllSeenCardsByTopic(topicId);

        List<Long> flashcards = seenCards.stream()
                .map(UserSeenCard::getCard)
                .map(Card::getFlashcard)
                .map(Flashcard::getId)
                .toList();

        return flashCardRepository.findByTopic(topic)
                .stream()
                .map(flashcard ->
                        new FlashcardUserResponse(
                                flashcard,
                                Math.toIntExact(cardCount.get(flashcard)),
                                flashcards.contains(flashcard.getId())))
                .toList();
    }

    public List<Flashcard> search(String search) {

        return flashCardRepository.search(search);
    }

    public void importExcel(Long lessonId, MultipartFile file) throws IOException {

        flashcardExcelImporter.saveExcel(lessonId, file);

    }
}
