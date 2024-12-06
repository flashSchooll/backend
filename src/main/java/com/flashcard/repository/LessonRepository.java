package com.flashcard.repository;

import com.flashcard.model.Lesson;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    Optional<Lesson> findByYksAndBranchAndYksLesson(YKS yks, Branch branch, YKSLesson yksLesson);
}
