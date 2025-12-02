package com.flashcard.service;

import com.flashcard.model.RepeatTopic;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.repository.RepeatTopicRepository;
import com.flashcard.security.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepeatTopicService {

    private final RepeatTopicRepository repeatTopicRepository;
    private final AuthService authService;
    private final TopicService topicService;

    @Transactional
    public RepeatTopic save(Long topicId, LocalDateTime repeatTime) {
        User user = authService.getCurrentUser();

        boolean isExists = repeatTopicRepository.existsByUserAndTopicId(user, topicId);

        RepeatTopic repeatTopic;
        if (isExists) {
            throw new IllegalArgumentException("Konu zaten kaydedilmiş");
        } else {
            Topic topic = topicService.getById(topicId);
            repeatTopic = new RepeatTopic();
            repeatTopic.setUser(user);
            repeatTopic.setTopic(topic);
            repeatTopic.setRepeatTime(repeatTime);
            repeatTopic.setDeleted(false);

            return repeatTopicRepository.save(repeatTopic);
        }
    }

    @Transactional
    public void delete(Long topicId) {
        User user = authService.getCurrentUser();

        RepeatTopic repeatTopic = repeatTopicRepository.findByUserAndTopicId(user, topicId);

        repeatTopicRepository.delete(repeatTopic);
    }

    public List<RepeatTopic> findAllByUserAntTopicId() {
        User user = authService.getCurrentUser();

        return repeatTopicRepository.findByUser(user);
    }
}
