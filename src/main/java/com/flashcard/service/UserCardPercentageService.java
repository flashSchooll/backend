package com.flashcard.service;

import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTLesson;
import com.flashcard.model.User;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.repository.TYTCardRepository;
import com.flashcard.repository.TYTLessonRepository;
import com.flashcard.repository.UserCardPercentageRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserCardPercentageService {

    private final UserCardPercentageRepository userCardPercentageRepository;
    private final TYTLessonRepository tytLessonRepository;
    private final TYTCardRepository tytCardRepository;
    private final AuthService authService;

    @Transactional
    public void save(User user) {

        List<TYTLesson> lessonList = tytLessonRepository.findAll();

        List<UserCardPercentage> percentageList = new ArrayList<>();
        UserCardPercentage percentage;
        for (TYTLesson lesson : lessonList) {
            int cardCount = tytCardRepository.countByTYTFlashcardTopicTytLesson(lesson);

            percentage = new UserCardPercentage();
            percentage.setUser(user);
            percentage.setCompletedCard(0);
            percentage.setLesson(lesson);
            percentage.setTotalCard(cardCount);

            percentageList.add(percentage);

        }

        userCardPercentageRepository.saveAll(percentageList);
    }

    public List<UserCardPercentage> getAllTYT() {

        User user = authService.getCurrentUser();

        return userCardPercentageRepository.findByUser(user);

    }

    @Transactional
    public void updatePercentage(User user, TYTFlashcard flashcard, int amount) {

        TYTLesson lesson = flashcard.getTopic().getTytLesson();

        UserCardPercentage userCardPercentage = userCardPercentageRepository.findByUserAndLesson(user, lesson)
                .orElseThrow(() -> new NoSuchElementException("Öğrenci yüzdelik kart bilgisi bulunamadı"));

        userCardPercentage.increaseCompletedCard(amount);

        userCardPercentageRepository.save(userCardPercentage);
    }
}
