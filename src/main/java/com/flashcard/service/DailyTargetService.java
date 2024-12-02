package com.flashcard.service;

import com.flashcard.controller.dailytarget.response.DailyTargetResponse;
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

import java.time.LocalDate;
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

    public DailyTargetResponse getTarget() {
        LocalDate today = LocalDate.now();

        Optional<DailyTarget> optionalDailyTarget = dailyTargetRepository.findByDay(today);

        return optionalDailyTarget.map(DailyTargetResponse::new).orElseGet(() -> new DailyTargetResponse(createTarget()));
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
}
