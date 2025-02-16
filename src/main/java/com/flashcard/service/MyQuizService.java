package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.quiz.request.QuizSaveRequest;
import com.flashcard.model.MyQuiz;
import com.flashcard.model.Quiz;
import com.flashcard.model.User;
import com.flashcard.repository.MyQuizRepository;
import com.flashcard.repository.QuizRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyQuizService {
    private final MyQuizRepository myQuizRepository;
    private final AuthService authService;
    private final QuizRepository quizRepository;

    @Transactional
    public void saveMyQuiz(QuizSaveRequest quizSaveRequest) {

        User user = authService.getCurrentUser();

        Quiz quiz = quizRepository.findById(quizSaveRequest.getQuizId())
                .orElseThrow(() -> new NoSuchElementException(Constants.QUIZ_NOT_FOUND));

        MyQuiz myQuiz = new MyQuiz();

        myQuiz.setUser(user);
        myQuiz.setOption(quizSaveRequest.getOption());
        myQuiz.setQuiz(quiz);

        myQuizRepository.save(myQuiz);
    }

    @Transactional
    public void deleteMyQuiz(Long quizId) {

        User user = authService.getCurrentUser();
        MyQuiz myQuiz = myQuizRepository.findByQuizId(quizId)
                .orElseThrow(() -> new NoSuchElementException(Constants.MY_QUIZ_NOT_FOUND));

        if (!Objects.equals(user.getId(), myQuiz.getUser().getId())) {
            throw new IllegalArgumentException("Kullanıcı başkasının kayıtlı quiz bilgisini silemez");
        }

        myQuizRepository.delete(myQuiz);
    }

  //  @Cacheable(value = "myQuizes", key = "#userId")
    public List<MyQuiz> myQuizes(Long userId) {

        return myQuizRepository.findByUserId(userId);
    }

}
