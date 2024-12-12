package com.flashcard.controller.statistic.user;

import com.flashcard.controller.dailytarget.response.DailyTargetStatisticResponse;
import com.flashcard.controller.statistic.response.UserCardStatisticResponse;
import com.flashcard.controller.statistic.response.UserStatisticLessonResponse;
import com.flashcard.model.DailyTarget;
import com.flashcard.service.CardService;
import com.flashcard.service.DailyTargetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistic/user")
@RequiredArgsConstructor
public class UserStatisticController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatisticController.class);

    private final DailyTargetService dailyTargetService;
    private final CardService cardService;


    @GetMapping("/weekly")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<DailyTargetStatisticResponse>> getWeekly() {
        List<DailyTarget> dailyTargets = dailyTargetService.getWeeklyTargets();

        List<DailyTargetStatisticResponse> response = dailyTargets.stream().map(DailyTargetStatisticResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/card")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserCardStatisticResponse> getCards() {
        UserCardStatisticResponse response = cardService.getUserStatistic();
        LOGGER.info("UserCardStatisticResponse: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserStatisticLessonResponse>> getCardsBylesson() {

        List<UserStatisticLessonResponse> response = cardService.getUserStatisticByLesson();

        return ResponseEntity.ok(response);
    }


}
