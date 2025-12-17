package com.flashcard.service;

import com.flashcard.controller.aiquestionpool.request.AIQuestionUserSaveRequest;
import com.flashcard.controller.aiquestionpool.response.AIQuestionUserResponse;
import com.flashcard.model.AIQuestion;
import com.flashcard.model.AIQuestionUser;
import com.flashcard.model.User;
import com.flashcard.repository.AIQuestionRepository;
import com.flashcard.repository.AIQuestionUserRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIQuestionUserService {
    private final AIQuestionUserRepository aiQuestionPoolRepository;
    private final AuthService authService;
    private final TopicRepository topicRepository;
    private final AIQuestionRepository aiQuestionRepository;

    @Transactional
    public void save(List<AIQuestionUserSaveRequest> request) {
        User currentUser = authService.getCurrentUser();

        List<AIQuestionUser> aiQuestionPools = new ArrayList<>();

        for (AIQuestionUserSaveRequest ai : request) {
            AIQuestion aiQuestion = aiQuestionRepository.findById(ai.getAiQuestionId()).orElseThrow(() -> new EntityNotFoundException("AI question id not found"));

            AIQuestionUser aiquestionUser = new AIQuestionUser();
            aiquestionUser.setAiQuestion(aiQuestion);
            aiquestionUser.setAnsweredUser(currentUser);
            aiquestionUser.setTimeSpent(ai.getTimeSpent());
            aiquestionUser.setUserAnswer(ai.getUserAnswer());
            aiquestionUser.setDone(false);
        }

        aiQuestionPoolRepository.saveAll(aiQuestionPools);
    }

    public List<AIQuestionUserResponse> getUsersWrongQuestionsGroupedByTestTitle() {

        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() != DayOfWeek.SATURDAY) {
            return Collections.emptyList();
        }

        LocalDate lastSunday = today.minusDays(6);

        User currentUser = authService.getCurrentUser();

        List<AIQuestionUser> aiQuestionPoolList = aiQuestionPoolRepository
                .findByAnsweredUserAndDoneAndCreatedDateBetween(
                        currentUser,
                        false,
                        lastSunday,
                        today
                );

        return aiQuestionPoolList.stream().map(AIQuestionUserResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void updateQuestions(List<String> request) {
        User currentUser = authService.getCurrentUser();
        aiQuestionPoolRepository.updateUserQuestions(currentUser, request);
    }
}
