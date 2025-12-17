package com.flashcard.repository;

import com.flashcard.model.AIQuestionUser;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AIQuestionUserRepository extends JpaRepository<AIQuestionUser, String> {
    List<AIQuestionUser> findByAnsweredUserAndDoneAndCreatedDateBetween(
            User answeredUser,
            boolean done,
            LocalDate startDate,
            LocalDate endDate
    );

    @Modifying
    @Query("""
            UPDATE AIQuestionUser SET done = TRUE WHERE answeredUser = :currentUser and aiQuestion.id in(:request)
            """)
    void updateUserQuestions(User currentUser, List<String> request);
}
