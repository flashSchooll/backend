package com.flashcard.service;

import com.flashcard.controller.growingreport.request.GrowingReportRequest;
import com.flashcard.controller.growingreport.response.GrowingReportLessonResponse;
import com.flashcard.controller.growingreport.response.GrowingReportResponse;
import com.flashcard.controller.growingreport.response.GrowingReportTopicResponse;
import com.flashcard.model.Lesson;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserQuizAnswer;
import com.flashcard.repository.UserQuizAnswerRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowingReportService {
    private final UserQuizAnswerRepository userQuizAnswerRepository;
    private final AuthService authService;


    public GrowingReportResponse getReportByUser(GrowingReportRequest request) {
        User user = authService.getCurrentUser();

        LocalDate startDate = request != null ? request.getStartDate() : null;
        LocalDate endDate = request != null ? request.getEndDate() : null;

        List<UserQuizAnswer> userQuizAnswers = userQuizAnswerRepository.findByUserAndDateRange(user, startDate, endDate);

        Map<Lesson, Map<Topic, List<UserQuizAnswer>>> groupedData = userQuizAnswers.stream()
                .filter(answer -> !answer.isDeleted())
                .filter(answer -> answer.getCreatedDate() != null)
                .filter(answer -> answer.getQuiz() != null && !answer.getQuiz().isDeleted())
                .filter(answer -> answer.getQuiz().getTopic() != null && !answer.getQuiz().getTopic().isDeleted())
                .filter(answer -> answer.getQuiz().getTopic().getLesson() != null)
                .collect(Collectors.groupingBy(
                        answer -> answer.getQuiz().getTopic().getLesson(),
                        Collectors.groupingBy(answer -> answer.getQuiz().getTopic())
                ));

        List<GrowingReportLessonResponse> lessonResponses = groupedData.entrySet().stream()
                .map(lessonEntry -> {
                    Lesson lesson = lessonEntry.getKey();

                    List<GrowingReportTopicResponse> topicResponses = lessonEntry.getValue().entrySet().stream()
                            .map(topicEntry -> {
                                Topic topic = topicEntry.getKey();
                                List<UserQuizAnswer> answers = topicEntry.getValue();

                                int questionCount = answers.size();
                                int rightCount = (int) answers.stream()
                                        .filter(answer -> answer.getAnswer() == answer.getQuiz().getAnswer())
                                        .count();

                                return getGrowingReportTopicResponse(questionCount, rightCount, topic);
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
