package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserFillBlankQuiz;
import com.flashcard.repository.FillBlankQuizRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserFillBlankQuizRepository;
import com.flashcard.repository.UserRepository;
import com.flashcard.security.services.AuthService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFillBlankQuizService {

    private final UserFillBlankQuizRepository userFillBlankQuizRepository;
    private final AuthService authService;
    private final TopicRepository topicRepository;
    private final FillBlankQuizRepository fillBlankQuizRepository;
    private final UserRepository userRepository;


    @Transactional
    public void save(@NotBlank String title, @NotNull Long topicId, Integer known) {

        User user = authService.getCurrentUser();
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        int count = fillBlankQuizRepository.countByTopicAndTitle(topic, title);

        Optional<UserFillBlankQuiz> optionalUserFillBlankQuiz = userFillBlankQuizRepository.findByUserAndTitleAndTopic(user, title, topic);
        if (optionalUserFillBlankQuiz.isEmpty()) {
            if (count == 0) {
                throw new IllegalArgumentException(String.format("Yanlış başlık değeri gönderdiniz %s", title));
            }

            UserFillBlankQuiz quiz = new UserFillBlankQuiz();
            quiz.setUser(user);
            quiz.setTopic(topic);
            quiz.setTitle(title);
            quiz.setKnown(known);
            quiz.setDeleted(false);

            userFillBlankQuizRepository.save(quiz);

            user.raiseStar(known);
            user.raiseRosette();

            userRepository.save(user);
        } else {
            int alreadyKnown = optionalUserFillBlankQuiz.get().getKnown();
            int totalKnown = alreadyKnown - known;

            user.raiseStar(totalKnown);

            userRepository.save(user);
        }

    }

}
