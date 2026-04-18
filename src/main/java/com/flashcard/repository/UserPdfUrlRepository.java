package com.flashcard.repository;

import com.flashcard.model.User;
import com.flashcard.model.UserPdfUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPdfUrlRepository extends JpaRepository<UserPdfUrl, UUID> {
    List<UserPdfUrl> findByUser(User user);

    Optional<UserPdfUrl> findByUserAndId(User user, UUID id);
}
