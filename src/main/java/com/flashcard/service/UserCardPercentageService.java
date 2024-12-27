package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.*;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.LessonRepository;
import com.flashcard.repository.UserCardPercentageRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCardPercentageService {

    private final UserCardPercentageRepository userCardPercentageRepository;
    private final LessonRepository lessonRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;
    private final ApplicationContext applicationContext;

    @Transactional
    public void save(User user, YKS yks, Branch branch) {

        List<Card> cards = cardRepository.findAll();
        Map<Lesson, Long> lessonCountMap = cards.stream()
                .collect(Collectors.groupingBy(
                        card -> card.getFlashcard().getTopic().getLesson(),
                        Collectors.counting()));

        List<Lesson> lessonList;

        if (yks.equals(YKS.TYT)) {
            lessonList = cards.stream()
                    .map(card -> card.getFlashcard().getTopic().getLesson())
                    .distinct()
                    .filter(lesson -> lesson.getYks().equals(YKS.TYT))
                    .collect(Collectors.toList());

            saveCardPercentage(user, lessonList, lessonCountMap);
        } else if (yks.equals(YKS.AYT)) {
            lessonList = cards.stream()
                    .map(card -> card.getFlashcard().getTopic().getLesson())
                    .distinct()
                    .filter(lesson -> lesson.getYks().equals(YKS.AYT) && lesson.getBranch().equals(branch))
                    .collect(Collectors.toList());

            saveCardPercentage(user, lessonList, lessonCountMap);
        } else {
            throw new IllegalArgumentException(Constants.WRONG_PARAMETER);
        }
    }

    void saveCardPercentage(User user, List<Lesson> lessonList, Map<Lesson, Long> lessonCountMap) {
        List<UserCardPercentage> percentageList = lessonList.stream()
                .map(lesson -> {
                    int cardCount = Math.toIntExact(lessonCountMap.getOrDefault(lesson, 0L));  // null olmasi durumunda 0 döndür
                    UserCardPercentage percentage = new UserCardPercentage();
                    percentage.setUser(user);
                    percentage.setCompletedCard(0);
                    percentage.setLesson(lesson);
                    percentage.setTotalCard(cardCount);
                    return percentage;
                })
                .collect(Collectors.toList());

        userCardPercentageRepository.saveAll(percentageList);
    }


    public List<UserCardPercentage> getTyt() {

        User user = authService.getCurrentUser();

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByUserAndLessonYks(user, YKS.TYT);

        if (!percentageList.isEmpty()) {
            return percentageList;
        } else {
            UserCardPercentageService proxy = applicationContext.getBean(UserCardPercentageService.class);
            proxy.save(user, YKS.TYT, null);
        }
        return userCardPercentageRepository.findByUserAndLessonYks(user, YKS.TYT);

    }

    public List<UserCardPercentage> getAyt() {

        User user = authService.getCurrentUser();

        Branch branch = user.getBranch();

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByUserAndLessonYksAndLessonBranch(user, YKS.AYT, branch);

        if (!percentageList.isEmpty()) {
            return percentageList;
        } else {
            UserCardPercentageService proxy = applicationContext.getBean(UserCardPercentageService.class);
            proxy.save(user, YKS.AYT, branch);
        }
        return userCardPercentageRepository.findByUserAndLessonYksAndLessonBranch(user, YKS.AYT, branch);

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
