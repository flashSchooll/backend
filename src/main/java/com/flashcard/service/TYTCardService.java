package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tytcard.admin.request.TYTCardSaveAllRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardSaveRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardUpdateRequest;
import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.model.ImageData;
import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.User;
import com.flashcard.model.enums.CardFace;
import com.flashcard.model.enums.DifficultyLevel;
import com.flashcard.repository.TYTCardRepository;
import com.flashcard.repository.TYTFlashCardRepository;
import com.flashcard.repository.UserCardSeenRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TYTCardService {

    private final TYTCardRepository tytCardRepository;
    private final TYTFlashCardService flashCardService;
    private final TYTFlashCardRepository tytFlashCardRepository;
    private final ApplicationContext applicationContext;
    private final UserCardSeenRepository userCardSeenRepository;
    private final AuthService authService;

    @Transactional
    public TYTCard save(TYTCardSaveRequest tytCardSaveRequest) throws BadRequestException {
        // Null kontrolü
        Long flashcardId = tytCardSaveRequest.getTytFlashcardId();
        Objects.requireNonNull(flashcardId, "Flashcard ID cannot be null");

        // Flashcard verisini al, var mı kontrol et
        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        // Kart sayısını kontrol et, limit aşıldıysa hata fırlat
        if (tytCardRepository.countByTytFlashcard(flashcard) > 20) {
            throw new BadRequestException(Constants.FLASHCARD_CAN_HAVE_20_CARDS);
        }

        // Görselleri işleyip listeye ekle
        List<ImageData> imageDataList = createImageDataList(tytCardSaveRequest);

        // Yeni TYTCard nesnesini oluştur
        TYTCard tytCard = new TYTCard();
        tytCard.setTytFlashcard(flashcard);
        tytCard.setBackFace(tytCardSaveRequest.getBackFace());
        tytCard.setFrontFace(tytCardSaveRequest.getFrontFace());
        tytCard.setImageData(imageDataList);

        // TYTCard'ı veritabanına kaydet
        return tytCardRepository.save(tytCard);
    }

    private List<ImageData> createImageDataList(TYTCardSaveRequest tytCardSaveRequest) throws BadRequestException {
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
    public TYTCard update(TYTCardUpdateRequest tytCardUpdateRequest) throws IOException {

        Objects.requireNonNull(tytCardUpdateRequest.getId());

        TYTCard tytCard = tytCardRepository.findById(tytCardUpdateRequest.getId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

        ImageData imageData;

        List<ImageData> imageDataList = new ArrayList<>();

        if (tytCardUpdateRequest.getFrontFile() != null) {
            imageData = new ImageData();
            imageData.setData(tytCardUpdateRequest.getFrontFile().getBytes());
            imageData.setFace(CardFace.FRONT);
            imageDataList.add(imageData);
        }

        if (tytCardUpdateRequest.getBackFile() != null) {
            imageData = new ImageData();
            imageData.setData(tytCardUpdateRequest.getBackFile().getBytes());
            imageData.setFace(CardFace.BACK);
            imageDataList.add(imageData);
        }

        tytCard.setBackFace(tytCardUpdateRequest.getBackFace());
        tytCard.setFrontFace(tytCardUpdateRequest.getFrontFace());
        tytCard.setImageData(imageDataList);

        return tytCardRepository.save(tytCard);
    }

    @Transactional
    public void delete(Long id) {

        Objects.requireNonNull(id);

        TYTCard tytCard = tytCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

        tytCardRepository.delete(tytCard);
    }

    public List<TYTCard> getAll(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        return tytCardRepository.findByTytFlashcard(flashcard);
    }

    @Transactional
    public List<TYTCard> saveAll(Long flashcardId, TYTCardSaveAllRequest request) throws IOException {
        Objects.requireNonNull(flashcardId);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        if (request.getTytCardSaveRequests().size() > 20) {
            throw new BadRequestException(Constants.FLASHCARD_CAN_HAVE_20_CARDS);
        }

        for (TYTCardSaveRequest saveRequest : request.getTytCardSaveRequests()) {
            TYTCardService proxy = applicationContext.getBean(TYTCardService.class);
            proxy.save(saveRequest);
        }

        List<TYTCard> cardList = tytCardRepository.findByTytFlashcard(flashcard);

        return cardList;
    }

    public List<TYTCard> explore() {

        long cardCount = tytCardRepository.count();

        Random random = new Random();
        Set<Long> uniqueNumbers = new HashSet<>();

        // Set'in boyutu istenilen sayıya ulaşana kadar rastgele sayılar ekle
        while (uniqueNumbers.size() < 100) {
            int randomNumber = random.nextInt((int) cardCount) + 1;  // 1 ile n arasında rastgele sayı
            uniqueNumbers.add((long) randomNumber);
        }

        List<Long> idList = uniqueNumbers.stream().toList();

        return tytCardRepository.findAllById(idList);
    }

    public List<TYTCard> exploreForMe(Boolean stateOfKnowledge, DifficultyLevel difficultyLevel) {

        User user = authService.getCurrentUser();

        List<TYTCard> cards = userCardSeenRepository.findByUser(user, stateOfKnowledge, difficultyLevel);

        long quantity;

        if (cards.size() >= 100) {
            quantity = 100;
        } else {
            quantity = (long) (cards.size() * 0.7);
        }

        Random random = new Random();

        return cards.stream()   //todo burayı tekrar test et
                .sorted((a, b) -> random.nextInt(2) - 1)  // Rastgele sıralama
                .limit(quantity)  // Belirtilen sayıda kart al
                .toList();
    }
}
