package com.flashcard.repository;

import com.flashcard.model.PasswordResetCode;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetCode,Long> {

    Optional<PasswordResetCode> findByUserAndCode(User user, String code);
}