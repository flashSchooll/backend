package com.flashcard.service.schedule;

import com.flashcard.model.DailyTarget;
import com.flashcard.model.MonthDailyTarget;
import com.flashcard.model.User;
import com.flashcard.repository.DailyTargetRepository;
import com.flashcard.repository.MonthDailyTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class DailyTargetSchedular {

    private final MonthDailyTargetRepository monthDailyTargetRepository;
    private final DailyTargetRepository dailyTargetRepository;


    @Scheduled(cron = "0 1 0 1 * ?",zone = "Europe/Istanbul")// her ayın 1 inde 00:01 de çalışır
    public void saveTargets() {

        List<DailyTarget> dailyTargets = dailyTargetRepository.findAll();

        LocalDate date = LocalDate.now();

        Month month = date.getMonth().minus(1);

        int year = date.getMonth() == Month.JANUARY ? (date.getYear() - 1) : date.getYear();
        boolean isLeapYear = Year.isLeap(year);

        Map<User, List<DailyTarget>> targetMap = dailyTargets.stream()
                .collect(Collectors.groupingBy(DailyTarget::getUser));

        List<MonthDailyTarget> monthDailyTargets = new ArrayList<>();

        for (Map.Entry<User, List<DailyTarget>> entry : targetMap.entrySet()) {
            User user = entry.getKey();
            List<DailyTarget> userTargets = entry.getValue();

            Integer average = userTargets.stream().mapToInt(DailyTarget::getMade).sum() / month.length(isLeapYear);

            MonthDailyTarget monthDailyTarget = MonthDailyTarget.builder()
                    .month(month)
                    .user(user)
                    .average(average)
                    .build();

            monthDailyTargets.add(monthDailyTarget);
        }

        monthDailyTargetRepository.saveAll(monthDailyTargets);
        dailyTargetRepository.deleteAll();
    }
}

