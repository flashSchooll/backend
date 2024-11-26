package com.flashcard.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flashcard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "where (:search is null or (u.userName ILIKE (%:search%) or u.userSurname ILIKE (%:search%)))")
    Page<User> findAllAsPage(String search, Pageable pageable);

}
