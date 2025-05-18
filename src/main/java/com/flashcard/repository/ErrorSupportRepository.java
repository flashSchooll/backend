package com.flashcard.repository;

import com.flashcard.model.ErrorSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ErrorSupportRepository extends JpaRepository<ErrorSupport, Long> {
    @Query("SELECT e FROM ErrorSupport e WHERE (:isSolved is null or e.solved = :isSolved)")
    Page<ErrorSupport> findAllAsPage(Boolean isSolved, Pageable pageable);
}
