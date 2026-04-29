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

    @Query(value = """
    SELECT * FROM user_fill_blank_quiz
    WHERE deleted = false
    AND user_id = :userId
    AND (CAST(:startDate AS date) IS NULL OR created_date >= CAST(:startDate AS date))
    AND (CAST(:endDate AS date) IS NULL OR created_date <= CAST(:endDate AS date))
    """, nativeQuery = true)
    List<UserFillBlankQuiz> findByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
