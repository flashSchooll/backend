package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.card.admin.request.CardSaveAllRequest;
import com.flashcard.controller.card.admin.request.CardSaveRequest;
import com.flashcard.controller.card.admin.request.CardUpdateRequest;
import com.flashcard.controller.statistic.response.UserCardStatisticResponse;
import com.flashcard.controller.statistic.response.UserStatisticLessonResponse;
import com.flashcard.model.*;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.CardFace;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.UserSeenCardRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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

    @Transactional
    public Card save(CardSaveRequest tytCardSaveRequest) throws BadRequestException {
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
        List<ImageData> imageDataList = createImageDataList(tytCardSaveRequest);

        // Yeni TYTCard nesnesini oluştur
        Card tytCard = new Card();
        tytCard.setFlashcard(flashcard);
        tytCard.setBackFace(tytCardSaveRequest.getBackFace());
        tytCard.setFrontFace(tytCardSaveRequest.getFrontFace());
        tytCard.setImageData(imageDataList);

        // TYTCard'ı veritabanına kaydet
        return cardRepository.save(tytCard);
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
    public Card update(CardUpdateRequest cardUpdateRequest) throws IOException {

        Objects.requireNonNull(cardUpdateRequest.getId());

        Card tytCard = cardRepository.findById(cardUpdateRequest.getId())
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        ImageData imageData;

        List<ImageData> imageDataList = new ArrayList<>();

        if (cardUpdateRequest.getFrontFile() != null) {
            imageData = new ImageData();
            imageData.setData(cardUpdateRequest.getFrontFile().getBytes());
            imageData.setFace(CardFace.FRONT);
            imageDataList.add(imageData);
        }

        if (cardUpdateRequest.getBackFile() != null) {
            imageData = new ImageData();
            imageData.setData(cardUpdateRequest.getBackFile().getBytes());
            imageData.setFace(CardFace.BACK);
            imageDataList.add(imageData);
        }

        tytCard.setBackFace(cardUpdateRequest.getBackFace());
        tytCard.setFrontFace(cardUpdateRequest.getFrontFace());
        tytCard.setImageData(imageDataList);

        return cardRepository.save(tytCard);
    }

    @Transactional
    public void delete(Long id) {

        Objects.requireNonNull(id);

        Card tytCard = cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

        cardRepository.delete(tytCard);
    }

    @Cacheable(value = "flashcardCard", key = "#flashcardId")
    public List<Card> getAll(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        return cardRepository.findByFlashcard(flashcard);
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

    @Cacheable(value = "explore")
    public List<Card> explore() {

        long cardCount = cardRepository.count();

        Set<Long> uniqueNumbers = new HashSet<>();

        // Set'in boyutu istenilen sayıya ulaşana kadar rastgele sayılar ekle
        while (uniqueNumbers.size() < 100) {
            int randomNumber = random.nextInt((int) cardCount) + 1;  // 1 ile n arasında rastgele sayı
            uniqueNumbers.add((long) randomNumber);
        }

        List<Long> idList = uniqueNumbers.stream().toList();

        return cardRepository.findAllById(idList);
    }

    public List<Card> exploreForMe() {

        User user = authService.getCurrentUser();

        Branch branch = user.getBranch();

        return cardRepository.findRandomCardsByBranch(branch != null ? branch.name() : null);
    }

    public UserCardStatisticResponse getUserStatistic() {

        User user = authService.getCurrentUser();

        int totalCountAyt = cardRepository.countByFlashcardTopicLessonYks(YKS.AYT);
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

        Branch branch = user.getBranch();

        List<UserSeenCard> cards = userSeenCardRepository.findByUser(user);

        Map<YKSLesson, Long> seenCardGroup = cards.stream()
                .filter(card -> card.getCard().getFlashcard().getTopic().getLesson().getBranch() == null
                        || card.getCard().getFlashcard().getTopic().getLesson().getBranch().equals(branch))
                .collect(Collectors.groupingBy(
                        card -> card.getCard().getFlashcard().getTopic().getLesson().getYksLesson(),
                        Collectors.counting())
                );

        List<Card> cardList = cardRepository.findAll();
        Map<YKSLesson, Long> cardGroup = cardList.stream()
                .collect(Collectors.groupingBy(
                        card -> card.getFlashcard().getTopic().getLesson().getYksLesson(),
                        Collectors.counting())
                );


        return cardGroup.keySet().stream()
                .map(aLong -> new UserStatisticLessonResponse(aLong.label,
                        seenCardGroup.get(aLong) == null ? 0 : seenCardGroup.get(aLong),
                        (seenCardGroup.get(aLong) != null ? ((double) seenCardGroup.get(aLong) / cardGroup.get(aLong)) : 0)))
                .toList();
    }

}
