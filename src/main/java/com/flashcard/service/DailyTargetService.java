package com.flashcard.service;

import com.flashcard.model.DailyTarget;
import com.flashcard.model.MonthDailyTarget;
import com.flashcard.model.User;
import com.flashcard.repository.DailyTargetRepository;
import com.flashcard.repository.MonthDailyTargetRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyTargetService {

    private final DailyTargetRepository dailyTargetRepository;
    private final AuthService authService;
    private final MonthDailyTargetRepository monthDailyTargetRepository;

    @Transactional
    public DailyTarget createTarget() {
        User user = authService.getCurrentUser();

        DailyTarget dailyTarget = new DailyTarget();
        dailyTarget.setUser(user);
        dailyTarget.setMade(0);
        dailyTarget.setDay(LocalDate.now());
        dailyTarget.setTarget(100);

        return dailyTargetRepository.save(dailyTarget);
    }

    public DailyTarget getTarget() {
        LocalDate today = LocalDate.now();

        User user = authService.getCurrentUser();

        Optional<DailyTarget> optionalDailyTarget = dailyTargetRepository.findByUserAndDay(user, today);

        return optionalDailyTarget.orElseGet(this::createTarget);
    }

    public Page<DailyTarget> getAllAdmin(String search, Pageable pageable) {

        return dailyTargetRepository.findBySearch(search, pageable);
    }

    public List<DailyTarget> getAll(String search) {

        return dailyTargetRepository.findBySearch(search);
    }

    public List<DailyTarget> getTargetMonthly() {
        User user = authService.getCurrentUser();

        return dailyTargetRepository.findByUser(user);

    }

    public List<MonthDailyTarget> getTargetPast() {
        User user = authService.getCurrentUser();

        return monthDailyTargetRepository.findByUser(user);
    }

    public List<DailyTarget> getWeeklyTargets() {

        LocalDate today = LocalDate.now();

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        User user = authService.getCurrentUser();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startOfWeek.format(formatter));
        LocalDate endDate = LocalDate.parse(today.format(formatter));

        return dailyTargetRepository.findByUserAndStartDateAndEndDate(user, startDate, endDate);
    }

    public void updateDailyTarget(int cardCount) {
        DailyTarget dailyTarget = getTarget();
        dailyTarget.updateMade(cardCount);

        dailyTargetRepository.save(dailyTarget);
    }
}
