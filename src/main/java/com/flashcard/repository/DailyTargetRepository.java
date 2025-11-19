package com.flashcard.repository;

import com.flashcard.model.DailyTarget;
import com.flashcard.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyTargetRepository extends JpaRepository<DailyTarget, Long> {

    Optional<DailyTarget> findByUserAndDay(User user, LocalDate today);

    @Query("SELECT d " +
            "FROM DailyTarget d " +
            "JOIN FETCH d.user u " +
            "WHERE (:search IS NULL OR " +
            "(u.userName ILIKE %:search% OR u.userSurname ILIKE %:search%))")
    List<DailyTarget> findBySearch(@Param("search") String search);



    @Query("SELECT d " +
            "FROM DailyTarget d " +
            "where (:search is null or (d.user.userName ILIKE (%:search%) or d.user.userSurname ILIKE (%:search%)))")
    Page<DailyTarget> findBySearch(String search, Pageable pageable);

    List<DailyTarget> findByUser(User user);

    @Query("SELECT d FROM DailyTarget d " +
            "WHERE (d.day BETWEEN :startDate AND :endDate) " +
            "AND d.user = :user " +
            "ORDER BY d.day")
    List<DailyTarget> findByUserAndStartDateAndEndDate(User user, LocalDate startDate, LocalDate endDate);

    List<DailyTarget> findAllByUserAndDayBetween(User user, LocalDate start, LocalDate end);

}
