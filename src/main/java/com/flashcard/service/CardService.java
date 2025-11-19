package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.card.admin.request.CardSaveAllRequest;
import com.flashcard.controller.card.admin.request.CardSaveRequest;
import com.flashcard.controller.card.admin.request.CardUpdateRequest;
import com.flashcard.controller.statistic.response.UserCardStatisticResponse;
import com.flashcard.controller.statistic.response.UserStatisticLessonResponse;
import com.flashcard.model.*;
import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.repository.*;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
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
public class CardService {

    private static final Random random = new Random();
    private final CardRepository cardRepository;
    private final FlashCardRepository flashCardRepository;
    private final ApplicationContext applicationContext;
    private final UserSeenCardRepository userSeenCardRepository;
    private final AuthService authService;
    private final RepeatFlashcardRepository repeatFlashcardRepository;
    private final MyCardsRepository myCardsRepository;
    private final UserCardPercentageRepository userCardPercentageRepository;
    private final S3StorageService s3StorageService;

    @Transactional
    public Card save(CardSaveRequest cardSaveRequest, MultipartFile frontFile, MultipartFile backFile) throws IOException {
        // Null kontrolü
        Long flashcardId = cardSaveRequest.getTytFlashcardId();
        Objects.requireNonNull(flashcardId, "Flashcard ID cannot be null");

        // Flashcard verisini al, var mı kontrol et
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        // Kart sayısını kontrol et, limit aşıldıysa hata fırlat
        if (cardRepository.countByFlashcard(flashcard) > 20) {
            throw new BadRequestException(Constants.FLASHCARD_CAN_HAVE_20_CARDS);
        }

        // Görselleri işleyip listeye ekle
        String frontPath = null;
        String backPath = null;

        if (frontFile != null) {
            frontPath = s3StorageService.uploadFile(frontFile, AWSDirectory.CARDS);
        }
        if (backFile != null) {
            backPath = s3StorageService.uploadFile(backFile, AWSDirectory.CARDS);

        }

        // Yeni TYTCard nesnesini oluştur
        Card card = new Card();
        card.setFlashcard(flashcard);
        card.setBackFace(cardSaveRequest.getBackFace());
        card.setFrontFace(cardSaveRequest.getFrontFace());
        card.setFrontPhotoPath(frontPath);
        card.setBackPhotoPath(backPath);

        // TYTCard'ı veritabanına kaydet
        return cardRepository.save(card);
    }

    @Transactional
    @CacheEvict(value = "cardCache", key = "#id")
    public Card update(CardUpdateRequest cardUpdateRequest, Long id, MultipartFile frontFile, MultipartFile backFile) throws IOException {

        Objects.requireNonNull(id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        if (frontFile != null) {

            String frontPath = s3StorageService.uploadFile(frontFile, AWSDirectory.CARDS);
            card.setFrontPhotoPath(frontPath);
        }

        if (backFile != null) {

            String frontPath = s3StorageService.uploadFile(backFile, AWSDirectory.CARDS);
            card.setBackPhotoPath(frontPath);
        }

        card.setBackFace(cardUpdateRequest.getBackFace());
        card.setFrontFace(cardUpdateRequest.getFrontFace());

        return cardRepository.save(card);
    }

    @Transactional
    @CacheEvict(value = "cardCache", key = "#id")
    public void delete(Long id) {

        Objects.requireNonNull(id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        cardRepository.delete(card);
    }


    public Page<Card> getAll(Flashcard flashcard, Pageable pageable) {

        return cardRepository.findCardsWithFlashcard(flashcard, pageable);
    }

    @Cacheable(value = "allCards", key = "#flashcard.id", unless = "#branch == null")
    public List<Card> getAll(Flashcard flashcard) {

        return cardRepository.findCardsWithFlashcard(flashcard);
    }

    @Transactional
    public List<Card> saveAll(Long flashcardId, CardSaveAllRequest request) throws IOException {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        if (request.getCardSaveRequests().size() > 20) {
            throw new BadRequestException(Constants.FLASHCARD_CAN_HAVE_20_CARDS);
        }

        for (CardSaveRequest saveRequest : request.getCardSaveRequests()) {
            CardService proxy = applicationContext.getBean(CardService.class);
            //   proxy.save(saveRequest);  todo
        }

        return cardRepository.findByFlashcard(flashcard);
    }

    /*   public List<Card> exploreForMe() {//todo bakılacak

           User user = authService.getCurrentUser();
           // RepeatFlashcard ve UserSeenCard tablolarından kartları al
           List<Card> myCards = myCardsRepository.findByUser(user).stream().map(MyCard::getCard).toList();


           List<Long> flashcards = repeatFlashcardRepository.findByUserWithTopicAndLesson(user).stream()
                   .map(repeatFlashcard -> repeatFlashcard.getFlashcards()) // RepeatFlashcard nesnesinden flashcards listesini alıyoruz
                   .flatMap(List::stream) // List içindeki tüm Flashcard'ları tek bir akışa düzleştiriyoruz
                   .map(Flashcard::getId) // Her bir Flashcard nesnesinin ID'sini alıyoruz
                   .collect(Collectors.toList()); // Akışı bir listeye topluyoruz

           // İki listeyi birleştir
           List<Card> combinedCards = new ArrayList<>();
           combinedCards.addAll(myCards);
           //     combinedCards.addAll(cardList);

           if (!combinedCards.isEmpty()) {
               // Rastgele 100 kart seç
               Collections.shuffle(combinedCards);
               return combinedCards.stream().limit(100).toList();
           } else {
               List<Card> userSeenCards = new ArrayList<>(userSeenCardRepository.findByUser(user).stream()
                       .map(UserSeenCard::getCard)
                       .toList());

               Collections.shuffle(userSeenCards);

               return userSeenCards.stream().limit(100).toList();
           }

       }
     */
    @Cacheable(value = "cardsCache", key = "#branch", unless = "#branch == null")
    public List<Card> explore(Branch branch) {

        return cardRepository.findRandomCardsByBranch(branch);
    }

    public List<Card> exploreWithoutCache(Branch branch) {

        return cardRepository.findRandomCardsByBranch(branch);
    }

    public UserCardStatisticResponse getUserCardStatistic() {

        User user = authService.getCurrentUser();
        Branch branch = user.getBranch();
        int totalCountAyt = cardRepository.countByFlashcardTopicLessonYksAndFlashcardTopicLessonBranchAndFlashcardCanBePublishTrue(YKS.AYT, branch);
        int totalCountTyt = cardRepository.countByFlashcardTopicLessonYksAndFlashcardCanBePublishTrue(YKS.TYT);
        int seenCard = userSeenCardRepository.countByUser(user);

        int totalCard = totalCountAyt + totalCountTyt;
        int unseenCard = totalCard - seenCard;
        double percentage = (double) seenCard / totalCard;

        return UserCardStatisticResponse
                .builder()
                .seenCard(seenCard)
                .percentage(percentage)
                .totalCard(totalCard)
                .unseenCard(unseenCard)
                .build();
    }

    public UserCardStatisticResponse getUserCardStatistic(YKS yks) {

        User user = authService.getCurrentUser();
        Branch branch = user.getBranch();

        int totalCount = 0;

        int seenCard;
        if (yks == YKS.AYT) {
            totalCount = cardRepository.countByFlashcardTopicLessonYksAndFlashcardTopicLessonBranchAndFlashcardCanBePublishTrue(YKS.AYT, branch);
            seenCard = userSeenCardRepository.countByUserAndCardFlashcardTopicLessonYks(user, yks);
        } else {
            totalCount = cardRepository.countByFlashcardTopicLessonYksAndFlashcardCanBePublishTrue(YKS.TYT);
            seenCard = userSeenCardRepository.countByUserAndCardFlashcardTopicLessonYks(user, yks);
        }

        int totalCard = totalCount;
        int unseenCard = totalCard - seenCard;
        double percentage = (double) seenCard / totalCard;

        return UserCardStatisticResponse
                .builder()
                .seenCard(seenCard)
                .percentage(percentage)
                .totalCard(totalCard)
                .unseenCard(unseenCard)
                .build();
    }

    public List<UserStatisticLessonResponse> getUserStatisticByLesson(YKS yks) {

        User user = authService.getCurrentUser();

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByUser(user, yks);

        return percentageList.stream().map(
                        l -> new UserStatisticLessonResponse(
                                l.getLesson().getYksLesson().label,
                                (long) l.getCompletedCard(),
                                (l.getCompletedCard() / (double) l.getTotalCard())))
                .toList();
    }

    public List<UserStatisticLessonResponse> getUserStatisticByLesson() {

        User user = authService.getCurrentUser();

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByUser(user);

        return percentageList.stream().map(
                        l -> new UserStatisticLessonResponse(
                                l.getLesson().getYksLesson().label,
                                (long) l.getCompletedCard(),
                                (l.getCompletedCard() / (double) l.getTotalCard())))
                .toList();
    }

    @Cacheable(value = "cardCache", key = "#cardId")
    public Card getCard(Long cardId) {
        Objects.requireNonNull(cardId);

        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));
    }

    public List<Card> getRandomCardsFromUserCollection() {
        // tekrar edeceklerim ve favoriler
        User user = authService.getCurrentUser();

        List<Card> allCards = cardRepository.getUserRepeatCardsAndMyCards(user.getId());

        return allCards.stream()
                .limit(Math.min(50, allCards.size()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFrontPath(Long id) {
        Objects.requireNonNull(id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        card.setFrontPhotoPath(null);

        cardRepository.save(card);
    }

    @Transactional
    public void deleteBackPath(Long id) {
        Objects.requireNonNull(id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        card.setBackPhotoPath(null);

        cardRepository.save(card);
    }
}
