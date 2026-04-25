package com.flashcard.repository;

import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserFillBlankQuiz;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserFillBlankQuizRepository extends JpaRepository<UserFillBlankQuiz, Long> {
    Long countByUserAndTitle(User user, String title);

    Optional<UserFillBlankQuiz> findByUserAndTitleAndTopic(User user, @NotBlank String title, Topic topic);

    @Query("""
    SELECT f FROM UserFillBlankQuiz f
    WHERE f.user = :user
    AND (:startDate IS NULL OR f.createdDate >= :startDate)
    AND (:endDate IS NULL OR f.createdDate <= :endDate)
    """)
    List<UserFillBlankQuiz> findByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
