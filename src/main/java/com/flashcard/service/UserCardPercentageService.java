package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.*;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.UserCardPercentageRepository;
import com.flashcard.repository.UserRepository;
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
    private final CardRepository cardRepository;
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;

    @Transactional
    public void save(User user, YKS yks, Branch branch) {

        List<Card> cards;
        if (branch == null) {
            cards = cardRepository.findByFlashcardTopicLessonYks(yks);
        } else {
            cards = cardRepository.findByFlashcardTopicLessonYksBranch(yks, branch);
        }

        Map<Lesson, Long> lessonCountMap = cards.stream()
                .collect(Collectors.groupingBy(
                        card -> card.getFlashcard().getTopic().getLesson(),
                        Collectors.counting()));

        List<Lesson> lessonList;
        UserCardPercentageService proxy = applicationContext.getBean(UserCardPercentageService.class);
        if (yks.equals(YKS.TYT)) {
            lessonList = cards.stream()
                    .map(card -> card.getFlashcard().getTopic().getLesson())
                    .distinct()
                    .filter(lesson -> lesson.getYks().equals(YKS.TYT))
                    .toList();

            proxy.saveCardPercentage(user, lessonList, lessonCountMap);

        } else if (yks.equals(YKS.AYT) && !cards.isEmpty()) {
            lessonList = cards.stream()
                    .map(card -> card.getFlashcard().getTopic().getLesson())
                    .distinct()
                    .filter(lesson -> lesson.getYks().equals(YKS.AYT) && lesson.getBranch().equals(branch))
                    .toList();

            proxy.saveCardPercentage(user, lessonList, lessonCountMap);

        } else {
            throw new IllegalArgumentException(Constants.WRONG_PARAMETER);
        }
    }

    @Transactional
    public void saveCardPercentage(User user, List<Lesson> lessonList, Map<Lesson, Long> lessonCountMap) {
        List<UserCardPercentage> percentageList = lessonList.stream()
                .map(lesson -> {
                    int cardCount = Math.toIntExact(lessonCountMap.getOrDefault(lesson, 0L));  // null olmasi durumunda 0 döndür
                    UserCardPercentage percentage = new UserCardPercentage();
                    percentage.setUser(user);
                    percentage.setCompletedCard(0);
                    percentage.setLesson(lesson);
                    percentage.setTotalCard(cardCount);
                    percentage.setDeleted(false);
                    return percentage;
                }).toList();

        userCardPercentageRepository.saveAll(percentageList);
    }

    //  @Cacheable(value = "userCardPercentageTyt", key = "#user.id")
    public List<UserCardPercentage> getTyt(User user) {

        List<UserCardPercentage> percentageList = userCardPercentageRepository.findByUserAndLessonYks(user, YKS.TYT);

        if (!percentageList.isEmpty()) {
            return percentageList;
        } else {
            UserCardPercentageService proxy = applicationContext.getBean(UserCardPercentageService.class);
            proxy.save(user, YKS.TYT, null);
        }
        return userCardPercentageRepository.findByUserAndLessonYks(user, YKS.TYT);

    }

    //   @Cacheable(value = "userCardPercentageAyt", key = "#user.id")
    public List<UserCardPercentage> getAyt(User user) {

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
    public void updateCardCount(Lesson lesson) {

        int count = cardRepository.countByFlashcardTopicLesson(lesson);

        int percentageCount = userCardPercentageRepository.countByLesson(lesson);

        if (percentageCount == 0) {
            UserCardPercentageService proxy = applicationContext.getBean(UserCardPercentageService.class);
            proxy.saveForLesson(lesson);
        } else {
            userCardPercentageRepository.updateTotalCardByLesson(lesson, count);
        }
    }

    //  @Cacheable(value = "countAverageFifty")
    public long countAverageFifty() {

        List<UserCardPercentage> percentages = userCardPercentageRepository.findAll();

        return percentages
                .stream()
                .filter(percentage -> ((double) percentage.getCompletedCard() / percentage.getTotalCard()) >= 50)
                .count();
    }

    @Transactional
    public void saveForLesson(Lesson lesson) {

        long countCard = cardRepository.countByFlashcardTopicLesson(lesson);

        List<User> users = userRepository.findAll();

        List<UserCardPercentage> percentages = new ArrayList<>();

        UserCardPercentage cardPercentage;

        for (User u : users) {
            cardPercentage = new UserCardPercentage();
            cardPercentage.setUser(u);
            cardPercentage.setCompletedCard(0);
            cardPercentage.setLesson(lesson);
            cardPercentage.setTotalCard((int) countCard);
            cardPercentage.setDeleted(false);

            percentages.add(cardPercentage);
        }

        userCardPercentageRepository.saveAll(percentages);
    }
}
