package com.flashcard.service;

import com.flashcard.controller.aiquestion.request.AIQuestionSaveRequest;
import com.flashcard.controller.aiquestion.request.AIQuestionUpdateRequest;
import com.flashcard.model.AIQuestion;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.repository.AIQuestionRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Service
public class AIQuestionService {
    private final AIQuestionRepository aiQuestionRepository;
    private final TopicService topicService;
    private final AuthService authService;

    @Transactional
    public void save(AIQuestionSaveRequest aiQuestionSaveRequest, String uuid) {
        Topic topic = topicService.getById(aiQuestionSaveRequest.getTopicId());
        User user = authService.getCurrentUser();

        AIQuestion aiQuestion = new AIQuestion();
        aiQuestion.setTopic(topic);
        aiQuestion.setUser(user);
        aiQuestion.setSubject(aiQuestionSaveRequest.getSubject());
        aiQuestion.setQuestion(aiQuestionSaveRequest.getQuestion());
        aiQuestion.setAnswer(aiQuestionSaveRequest.getAnswer());
        aiQuestion.setA(aiQuestionSaveRequest.getOptions().get(0));
        aiQuestion.setB(aiQuestionSaveRequest.getOptions().get(1));
        aiQuestion.setC(aiQuestionSaveRequest.getOptions().get(2));
        aiQuestion.setD(aiQuestionSaveRequest.getOptions().get(3));
        if (aiQuestionSaveRequest.getOptions().size() == 5) {
            aiQuestion.setE(aiQuestionSaveRequest.getOptions().get(4));
        }
        aiQuestion.setLevel(aiQuestionSaveRequest.getLevel());
        aiQuestion.setDescription(aiQuestionSaveRequest.getDescription());
        aiQuestion.setPublished(false);
        aiQuestion.setDeleted(false);
        aiQuestion.setUuid(uuid);

        aiQuestionRepository.save(aiQuestion);
    }

    public List<AIQuestion> findAll() {
        User user = authService.getCurrentUser();

        return aiQuestionRepository.findAllByPublishedTrueAndUserNot(user);
    }

    public List<AIQuestion> findByTopic(Long topicId) {
        Topic topic = topicService.getById(topicId);
        User user = authService.getCurrentUser();

        return aiQuestionRepository.findByTopicAndPublishedTrueAndUserNot(topic, user);
    }

    public List<AIQuestion> findByAdmin() {
        return aiQuestionRepository.findAll();
    }

    @Transactional
    public AIQuestion publishAIQuestion(String aiQuestionId) {
        AIQuestion aiQuestion = aiQuestionRepository.findById(aiQuestionId).orElseThrow(() -> new EntityNotFoundException("aiQuestionId"));
        aiQuestion.setPublished(true);

        return aiQuestionRepository.save(aiQuestion);
    }

    public List<AIQuestion> findByAdminAndTopic(Long topicId) {
        Topic topic = topicService.getById(topicId);

        return aiQuestionRepository.findByTopic(topic);
    }

    public AIQuestion findById(String aiQuestionId) {
        return aiQuestionRepository.findById(aiQuestionId)
                .orElseThrow(() -> new EntityNotFoundException("aiQuestionId"));

    }

    @Transactional
    public void delete(String aiQuestionId) {
        AIQuestion aiQuestion = aiQuestionRepository.findById(aiQuestionId)
                .orElseThrow(() -> new EntityNotFoundException("aiQuestionId"));

        aiQuestionRepository.delete(aiQuestion);
    }

    public List<AIQuestion> findByCurrentUser() {
        User user = authService.getCurrentUser();

        return aiQuestionRepository.findByUser(user);
    }

    @Transactional
    public void saveAll(List<AIQuestionSaveRequest> aiQuestionSaveRequest) {
        String uuid = java.util.UUID.randomUUID().toString();

        for (AIQuestionSaveRequest request : aiQuestionSaveRequest) {
            save(request, uuid);
        }
    }

    @Transactional
    public AIQuestion update(String aiQuestionId, AIQuestionUpdateRequest aiQuestionUpdateRequest) {
        AIQuestion aiQuestion = aiQuestionRepository.findById(aiQuestionId)
                .orElseThrow(() -> new EntityNotFoundException("AIQuestion bulunamadı"));

        aiQuestion.setSubject(aiQuestionUpdateRequest.getSubject());
        aiQuestion.setQuestion(aiQuestionUpdateRequest.getQuestion());
        aiQuestion.setAnswer(aiQuestionUpdateRequest.getAnswer());
        aiQuestion.setA(aiQuestionUpdateRequest.getOptions().get(0));
        aiQuestion.setB(aiQuestionUpdateRequest.getOptions().get(1));
        aiQuestion.setC(aiQuestionUpdateRequest.getOptions().get(2));
        aiQuestion.setD(aiQuestionUpdateRequest.getOptions().get(3));
        if (aiQuestionUpdateRequest.getOptions().size() == 5) {
            aiQuestion.setE(aiQuestionUpdateRequest.getOptions().get(4));
        } else {
            aiQuestion.setE(null);
        }
        aiQuestion.setLevel(aiQuestionUpdateRequest.getLevel());
        aiQuestion.setDescription(aiQuestionUpdateRequest.getDescription());

        return aiQuestionRepository.save(aiQuestion);
    }
}
