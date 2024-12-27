package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.Flashcard;
import com.flashcard.model.Lesson;
import com.flashcard.model.User;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.model.enums.YKS;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.LessonRepository;
import com.flashcard.repository.UserCardPercentageRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserCardPercentageService {

    private final UserCardPercentageRepository userCardPercentageRepository;
    private final LessonRepository lessonRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;
    private final ApplicationContext applicationContext;

    @Transactional
    public void save(User user) {

        List<Lesson> lessonList = lessonRepository.findAll();

        List<UserCardPercentage> percentageList = new ArrayList<>();
        UserCardPercentage percentage;
        for (Lesson lesson : lessonList) {
            int cardCount = cardRepository.countByFlashcardTopicLesson(lesson);

            percentage = new UserCardPercentage();
            percentage.setUser(user);
            percentage.setCompletedCard(0);
            percentage.setLesson(lesson);
            percentage.setTotalCard(cardCount);

            percentageList.add(percentage);
        }

        userCardPercentageRepository.saveAll(percentageList);

    }

    public List<UserCardPercentage> getAllYks(YKS yks) {

        User user = authService.getCurrentUser();

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByUserAndLessonYks(user, yks);

        if (!percentageList.isEmpty()) {
            return percentageList;
        } else {
            UserCardPercentageService proxy = applicationContext.getBean(UserCardPercentageService.class);
            proxy.save(user);

        }
        return userCardPercentageRepository.findByUserAndLessonYks(user, yks);

    }
    @Transactional
    public void updatePercentage(User user, Flashcard flashcard, int amount) {

        Lesson lesson = flashcard.getTopic().getLesson();

        UserCardPercentage userCardPercentage = userCardPercentageRepository.findByUserAndLesson(user, lesson)
                .orElseThrow(() -> new NoSuchElementException(Constants.USER_PERCENTAGE_NOT_FOUND));

        if (userCardPercentage.getFlashCards() != null && !userCardPercentage.getFlashCards().contains(flashcard.getId())) {
            userCardPercentage.increaseCompletedCard(amount);
            Set<Long> flashcards = userCardPercentage.getFlashCards();
            flashcards.add(flashcard.getId());

            userCardPercentageRepository.save(userCardPercentage);
        }

    }

    @Transactional
    public void updateCardCount(Lesson lesson, int size) {

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByLesson(lesson);

        for (UserCardPercentage percentage : percentageList) {
            percentage.setTotalCard(percentage.getTotalCard() + size);
        }

        userCardPercentageRepository.saveAll(percentageList);
    }

    public long countAverageFifty() {

        List<UserCardPercentage> percentages = userCardPercentageRepository.findAll();

        return percentages
                .stream()
                .filter(percentage -> ((double) percentage.getCompletedCard() / percentage.getTotalCard()) >= 50)
                .count();
    }
}
