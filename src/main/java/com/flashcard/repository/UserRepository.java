package com.flashcard.repository;

import com.flashcard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("select u from User u order by u.star desc,u.rosette desc limit 20")
    List<User> findByStar();

    @Query(value = "WITH RankedUsers AS (" +
            "SELECT " +
            "u.id AS id, " +
            "u.star AS star, " +
            "u.rosette AS rosette, " +
            "ROW_NUMBER() OVER (ORDER BY u.star DESC, u.rosette DESC,u.user_name) AS rank " +
            "FROM users u" +
            ") " +
            "SELECT " +
            "rank " +
            "FROM RankedUsers " +
            "WHERE id = :id",
            nativeQuery = true)
    Long findOrderByUser(@Param("id") Long id);
}
