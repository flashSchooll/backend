package com.flashcard.service;

import com.flashcard.controller.growingreport.request.GrowingReportRequest;
import com.flashcard.controller.growingreport.response.GrowingReportLessonResponse;
import com.flashcard.controller.growingreport.response.GrowingReportResponse;
import com.flashcard.controller.growingreport.response.GrowingReportTopicResponse;
import com.flashcard.model.*;
import com.flashcard.repository.UserFillBlankQuizRepository;
import com.flashcard.repository.UserQuizAnswerRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowingReportService {
    private final UserQuizAnswerRepository userQuizAnswerRepository;
    private final UserFillBlankQuizRepository userFillBlankQuizRepository;
    private final AuthService authService;


    // todo test yapılacak
    public GrowingReportResponse getReportByUser(GrowingReportRequest request) {
        User user = authService.getCurrentUser();

        LocalDate startDate = request != null ? request.getStartDate() : null;
        LocalDate endDate = request != null ? request.getEndDate() : null;

        // Mevcut çoktan seçmeli cevaplar
        List<UserQuizAnswer> userQuizAnswers = userQuizAnswerRepository
                .findByUserAndDateRange(user, startDate, endDate);

        // Boşluk doldurma quizleri
        List<UserFillBlankQuiz> fillBlankQuizzes = userFillBlankQuizRepository
                .findByUserAndDateRange(user, startDate, endDate);

        // Çoktan seçmeli: Lesson → Topic → [answers]
        Map<Lesson, Map<Topic, List<UserQuizAnswer>>> groupedQuizData = userQuizAnswers.stream()
                .filter(answer -> !answer.isDeleted())
                .filter(answer -> answer.getCreatedDate() != null)
                .filter(answer -> answer.getQuiz() != null && !answer.getQuiz().isDeleted())
                .filter(answer -> answer.getQuiz().getTopic() != null && !answer.getQuiz().getTopic().isDeleted())
                .filter(answer -> answer.getQuiz().getTopic().getLesson() != null)
                .collect(Collectors.groupingBy(
                        answer -> answer.getQuiz().getTopic().getLesson(),
                        Collectors.groupingBy(answer -> answer.getQuiz().getTopic())
                ));

        // Boşluk doldurma: Lesson → Topic → [fillBlanks]
        Map<Lesson, Map<Topic, List<UserFillBlankQuiz>>> groupedFillBlankData = fillBlankQuizzes.stream()
                .filter(fb -> fb.getTopic() != null && !fb.getTopic().isDeleted())
                .filter(fb -> fb.getTopic().getLesson() != null)
                .collect(Collectors.groupingBy(
                        fb -> fb.getTopic().getLesson(),
                        Collectors.groupingBy(UserFillBlankQuiz::getTopic)
                ));

        // Tüm lesson key'lerini birleştir
        Set<Lesson> allLessons = new HashSet<>();
        allLessons.addAll(groupedQuizData.keySet());
        allLessons.addAll(groupedFillBlankData.keySet());

        List<GrowingReportLessonResponse> lessonResponses = allLessons.stream()
                .map(lesson -> {
                    // Tüm topic key'lerini birleştir
                    Map<Topic, List<UserQuizAnswer>> quizByTopic = groupedQuizData
                            .getOrDefault(lesson, Collections.emptyMap());
                    Map<Topic, List<UserFillBlankQuiz>> fillBlankByTopic = groupedFillBlankData
                            .getOrDefault(lesson, Collections.emptyMap());

                    Set<Topic> allTopics = new HashSet<>();
                    allTopics.addAll(quizByTopic.keySet());
                    allTopics.addAll(fillBlankByTopic.keySet());

                    List<GrowingReportTopicResponse> topicResponses = allTopics.stream()
                            .map(topic -> {
                                // Çoktan seçmeli istatistikler
                                List<UserQuizAnswer> answers = quizByTopic
                                        .getOrDefault(topic, Collections.emptyList());
                                int quizTotal = answers.size();
                                int quizRight = (int) answers.stream()
                                        .filter(a -> a.getAnswer() == a.getQuiz().getAnswer())
                                        .count();

                                // Boşluk doldurma istatistikler
                                List<UserFillBlankQuiz> fillBlanks = fillBlankByTopic
                                        .getOrDefault(topic, Collections.emptyList());
                                int fillBlankKnown = fillBlanks.stream()
                                        .mapToInt(fb -> fb.getKnown() != null ? fb.getKnown() : 0)
                                        .sum();
                                int fillBlankTotal = fillBlanks.stream()
                                        .mapToInt(fb -> fb.getTotal() != null ? fb.getTotal() : 0) // total alanı yoksa aşağıya bak
                                        .sum();

                                // Toplam
                                int totalQuestions = quizTotal + fillBlankTotal;
                                int totalRight = quizRight + fillBlankKnown;

                                return getGrowingReportTopicResponse(totalQuestions, totalRight, topic);
                            }).toList();

                    GrowingReportLessonResponse lessonResponse = new GrowingReportLessonResponse();
                    lessonResponse.setLesson(lesson.getYksLesson().name());
                    lessonResponse.setTopics(topicResponses);
                    return lessonResponse;
                }).toList();

        GrowingReportResponse response = new GrowingReportResponse();
        response.setLessons(lessonResponses);
        return response;
    }

    private static GrowingReportTopicResponse getGrowingReportTopicResponse(int questionCount, int rightCount, Topic topic) {
        int wrongCount = questionCount - rightCount;
        int percent = questionCount > 0 ? (rightCount * 100) / questionCount : 0;

        GrowingReportTopicResponse topicResponse = new GrowingReportTopicResponse();
        topicResponse.setTopic(topic.getSubject());
        topicResponse.setQuestionCount(questionCount);
        topicResponse.setRightCount(rightCount);
        topicResponse.setWrongCount(wrongCount);
        topicResponse.setPercent(percent);
        return topicResponse;
    }
}
