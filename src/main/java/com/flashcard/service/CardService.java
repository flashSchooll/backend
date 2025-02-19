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
import com.flashcard.model.enums.CardFace;
import com.flashcard.model.enums.YKS;
import com.flashcard.repository.*;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
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
    public Card save(CardSaveRequest tytCardSaveRequest) throws IOException {
        // Null kontrolü
        Long flashcardId = tytCardSaveRequest.getTytFlashcardId();
        Objects.requireNonNull(flashcardId, "Flashcard ID cannot be null");

        // Flashcard verisini al, var mı kontrol et
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        // Kart sayısını kontrol et, limit aşıldıysa hata fırlat
        if (cardRepository.countByFlashcard(flashcard) > 20) {
            throw new BadRequestException(Constants.FLASHCARD_CAN_HAVE_20_CARDS);
        }

        // Görselleri işleyip listeye ekle
        // List<ImageData> imageDataList = createImageDataList(tytCardSaveRequest);
        String frontPath = s3StorageService.uploadFile(tytCardSaveRequest.getFrontFile(), AWSDirectory.CARDS);
        String backPath = s3StorageService.uploadFile(tytCardSaveRequest.getBackFile(), AWSDirectory.CARDS);
        // Yeni TYTCard nesnesini oluştur
        Card card = new Card();
        card.setFlashcard(flashcard);
        card.setBackFace(tytCardSaveRequest.getBackFace());
        card.setFrontFace(tytCardSaveRequest.getFrontFace());
        card.setFrontPhotoPath(frontPath);
        card.setBackPhotoPath(backPath);
        //  tytCard.setImageData(imageDataList);

        // TYTCard'ı veritabanına kaydet
        return cardRepository.save(card);
    }

    private List<ImageData> createImageDataList(CardSaveRequest tytCardSaveRequest) throws BadRequestException {
        List<ImageData> imageDataList = new ArrayList<>();

        // Ön yüz dosyası varsa, onu ekle
        if (tytCardSaveRequest.getFrontFile() != null) {
            imageDataList.add(createImageData(tytCardSaveRequest.getFrontFile(), CardFace.FRONT));
        }

        // Arka yüz dosyası varsa, onu ekle
        if (tytCardSaveRequest.getBackFile() != null) {
            imageDataList.add(createImageData(tytCardSaveRequest.getBackFile(), CardFace.BACK));
        }

        return imageDataList;
    }

    private ImageData createImageData(MultipartFile file, CardFace face) throws BadRequestException {
        try {
            ImageData imageData = new ImageData();
            imageData.setData(file.getBytes());
            imageData.setFace(face);
            return imageData;
        } catch (IOException e) {
            throw new BadRequestException("Error processing image file: " + e.getMessage());
        }
    }

    @Transactional
    @CacheEvict(value = "cardCache", key = "#cardUpdateRequest.cardId")
    public Card update(CardUpdateRequest cardUpdateRequest) throws IOException {

        Objects.requireNonNull(cardUpdateRequest.getId());

        Card card = cardRepository.findById(cardUpdateRequest.getId())
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        ImageData imageData;

        List<ImageData> imageDataList = new ArrayList<>();

        if (cardUpdateRequest.getFrontFile() != null) {
            //  imageData = new ImageData();
            //  imageData.setData(cardUpdateRequest.getFrontFile().getBytes());
            //  imageData.setFace(CardFace.FRONT);
            //  imageDataList.add(imageData);
            String frontPath = s3StorageService.uploadFile(cardUpdateRequest.getFrontFile(), AWSDirectory.CARDS);
            card.setFrontPhotoPath(frontPath);

        }

        if (cardUpdateRequest.getBackFile() != null) {
            //   imageData = new ImageData();
            //   imageData.setData(cardUpdateRequest.getBackFile().getBytes());
            //   imageData.setFace(CardFace.BACK);
            //   imageDataList.add(imageData);
            String frontPath = s3StorageService.uploadFile(cardUpdateRequest.getBackFile(), AWSDirectory.CARDS);
            card.setBackPhotoPath(frontPath);

        }

        card.setBackFace(cardUpdateRequest.getBackFace());
        card.setFrontFace(cardUpdateRequest.getFrontFace());
      //  card.setImageData(imageDataList);

        return cardRepository.save(card);
    }

    @Transactional
    @CacheEvict(value = "cardCache", key = "#cardId")
    public void delete(Long id) {

        Objects.requireNonNull(id);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        cardRepository.delete(card);
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
            proxy.save(saveRequest);
        }

        return cardRepository.findByFlashcard(flashcard);
    }

    public List<Card> exploreForMe() {//todo bakılacak

        User user = authService.getCurrentUser();
        // RepeatFlashcard ve UserSeenCard tablolarından kartları al
        List<Card> repeatFlashcards = myCardsRepository.findByUser(user).stream().map(MyCard::getCard).toList();


        List<Long> flashcards = repeatFlashcardRepository.findByUserWithTopicAndLesson(user).stream()
                .map(repeatFlashcard -> repeatFlashcard.getFlashcards()) // RepeatFlashcard nesnesinden flashcards listesini alıyoruz
                .flatMap(List::stream) // List içindeki tüm Flashcard'ları tek bir akışa düzleştiriyoruz
                .map(flashcard -> flashcard.getId()) // Her bir Flashcard nesnesinin ID'sini alıyoruz
                .collect(Collectors.toList()); // Akışı bir listeye topluyoruz

        List<Card> cardList = cardRepository.findByFlashcardIn(flashcards); // todo sorguya bakılacak çok fazla istek atıyor

        // İki listeyi birleştir
        List<Card> combinedCards = new ArrayList<>();
        combinedCards.addAll(repeatFlashcards);
        combinedCards.addAll(cardList);

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

    @Cacheable(value = "cardsCache", key = "#branch", unless = "#branch == null")
    public List<Card> explore(String branch) {

        return cardRepository.findRandomCardsByBranch(branch);// TODO ÇOK İSTEK ATIYOR 100 TANE
    }

    public UserCardStatisticResponse getUserCardStatistic() {

        User user = authService.getCurrentUser();
        Branch branch = user.getBranch();
        int totalCountAyt = cardRepository.countByFlashcardTopicLessonYksAndFlashcardTopicLessonBranch(YKS.AYT, branch);
        int totalCountTyt = cardRepository.countByFlashcardTopicLessonYks(YKS.TYT);
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
}
