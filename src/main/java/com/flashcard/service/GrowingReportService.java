package com.flashcard.service;

import com.flashcard.controller.growingreport.request.GrowingReportRequest;
import com.flashcard.controller.growingreport.response.GrowingReportLessonResponse;
import com.flashcard.controller.growingreport.response.GrowingReportResponse;
import com.flashcard.controller.growingreport.response.GrowingReportTopicResponse;
import com.flashcard.model.*;
import com.flashcard.model.enums.QuizType;
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
                .findByUserAndDateRange(user.getId(), startDate, endDate);

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
                                List<UserQuizAnswer> answers = quizByTopic
                                        .getOrDefault(topic, Collections.emptyList());

                                // QuizType'a göre ayır
                                List<UserQuizAnswer> testAnswers = answers.stream()
                                        .filter(a -> a.getQuiz().getType() == QuizType.TEST)
                                        .toList();

                                List<UserQuizAnswer> rightWrongAnswers = answers.stream()
                                        .filter(a -> a.getQuiz().getType() == QuizType.RIGHT_WRONG)
                                        .toList();

                                // TEST istatistikleri
                                int testTotal = testAnswers.size();
                                int testRight = (int) testAnswers.stream()
                                        .filter(a -> a.getAnswer() == a.getQuiz().getAnswer())
                                        .count();

                                // RIGHT_WRONG istatistikleri
                                int rwTotal = rightWrongAnswers.size();
                                int rwRight = (int) rightWrongAnswers.stream()
                                        .filter(a -> a.getAnswer() == a.getQuiz().getAnswer())
                                        .count();

                                // FillBlank (mevcut)
                                List<UserFillBlankQuiz> fillBlanks = fillBlankByTopic
                                        .getOrDefault(topic, Collections.emptyList());
                                int fillBlankKnown = fillBlanks.stream()
                                        .mapToInt(fb -> fb.getKnown() != null ? fb.getKnown() : 0).sum();
                                int fillBlankTotal = fillBlanks.stream()
                                        .mapToInt(fb -> fb.getTotal() != null ? fb.getTotal() : 0).sum();

                                return buildTopicResponse(topic, testTotal, testRight,
                                        rwTotal, rwRight,
                                        fillBlankTotal, fillBlankKnown);
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

    private GrowingReportTopicResponse buildTopicResponse(
            Topic topic,
            int testTotal, int testRight,
            int rwTotal, int rwRight,
            int fillBlankTotal, int fillBlankKnown) {

        int testWrong = testTotal - testRight;
        int testPercent = testTotal > 0 ? (testRight * 100) / testTotal : 0;

        int rwWrong = rwTotal - rwRight;
        int rwPercent = rwTotal > 0 ? (rwRight * 100) / rwTotal : 0;

        // Genel toplam: fillBlank'i istediğin gruba ekleyebilirsin
        // ya da ayrı tutabilirsin. Burada genel toplama eklendi.
        int grandTotal = testTotal + rwTotal + fillBlankTotal;
        int grandRight = testRight + rwRight + fillBlankKnown;
        int grandWrong = grandTotal - grandRight;
        int grandPercent = grandTotal > 0 ? (grandRight * 100) / grandTotal : 0;

        GrowingReportTopicResponse response = new GrowingReportTopicResponse();
        response.setTopic(topic.getSubject());

        response.setTestQuestionCount(testTotal);
        response.setTestRightCount(testRight);
        response.setTestWrongCount(testWrong);
        response.setTestPercent(testPercent);

        response.setRightWrongQuestionCount(rwTotal);
        response.setRightWrongRightCount(rwRight);
        response.setRightWrongWrongCount(rwWrong);
        response.setRightWrongPercent(rwPercent);

        response.setTotalQuestionCount(grandTotal);
        response.setTotalRightCount(grandRight);
        response.setTotalWrongCount(grandWrong);
        response.setTotalPercent(grandPercent);

        return response;
    }
}
