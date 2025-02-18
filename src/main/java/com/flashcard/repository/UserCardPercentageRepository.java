package com.flashcard.repository;

import com.flashcard.model.Lesson;
import com.flashcard.model.User;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCardPercentageRepository extends JpaRepository<UserCardPercentage, Long> {

    @Query("SELECT ucp FROM UserCardPercentage ucp JOIN ucp.user u WHERE u = :user")
    @EntityGraph(attributePaths = {"lesson"})
    List<UserCardPercentage> findByUser(User user);

    Optional<UserCardPercentage> findByUserAndLesson(User user, Lesson lesson);

    List<UserCardPercentage> findByLesson(Lesson lesson);

    @EntityGraph(attributePaths = {"lesson"})
    List<UserCardPercentage> findByUserAndLessonYks(User user, YKS yks);

    List<UserCardPercentage> findByUserAndLessonYksAndLessonBranch(User user, YKS yks, Branch branch);

    @Modifying
    @Transactional
    @Query("UPDATE UserCardPercentage ucp SET ucp.totalCard = :total WHERE ucp.lesson = :lesson")
    void updateTotalCardByLesson(@Param("lesson") Lesson lesson, @Param("total") int total);

    int countByLesson(Lesson lesson);
}
