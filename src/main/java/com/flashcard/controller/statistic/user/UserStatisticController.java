package com.flashcard.controller.statistic.user;

import com.flashcard.controller.dailytarget.response.DailyTargetStatisticResponse;
import com.flashcard.controller.statistic.response.UserCardStatisticResponse;
import com.flashcard.controller.statistic.response.UserRosetteStatistic;
import com.flashcard.controller.statistic.response.UserStatisticAllResponse;
import com.flashcard.controller.statistic.response.UserStatisticLessonResponse;
import com.flashcard.model.DailyTarget;
import com.flashcard.model.enums.YKS;
import com.flashcard.service.CardService;
import com.flashcard.service.DailyTargetService;
import com.flashcard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/statistic/user")
@RequiredArgsConstructor
public class UserStatisticController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatisticController.class);

    private final DailyTargetService dailyTargetService;
    private final CardService cardService;
    private final UserService userService;


    @GetMapping("/weekly")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<DailyTargetStatisticResponse>> getWeekly() {
        List<DailyTarget> dailyTargets = dailyTargetService.getWeeklyTargets();

        List<DailyTargetStatisticResponse> response = dailyTargets.stream().map(DailyTargetStatisticResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/card")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserCardStatisticResponse> getCards(
            @RequestParam YKS yks
    ) {
        UserCardStatisticResponse response = cardService.getUserCardStatistic(yks);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson/ayt")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserStatisticLessonResponse>> getCardsByLessonAyt() {

        List<UserStatisticLessonResponse> response = cardService.getUserStatisticByLesson(YKS.AYT);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson/tyt")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserStatisticLessonResponse>> getCardsByLessonTyt() {

        List<UserStatisticLessonResponse> response = cardService.getUserStatisticByLesson(YKS.TYT);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> getAll() {
        List<DailyTarget> dailyTargets = dailyTargetService.getWeeklyTargets();

        List<DailyTargetStatisticResponse> dailyTargetStatisticResponseList = dailyTargets.stream().map(DailyTargetStatisticResponse::new).toList();
        UserCardStatisticResponse userCardStatisticResponse = cardService.getUserCardStatistic();
        List<UserStatisticLessonResponse> userStatisticLessonResponses = cardService.getUserStatisticByLesson();

        UserStatisticAllResponse allResponse = new UserStatisticAllResponse();
        allResponse.setUserStatisticLessonResponses(userStatisticLessonResponses);
        allResponse.setDailyTargetStatisticResponse(dailyTargetStatisticResponseList);
        allResponse.setUserCardStatisticResponse(userCardStatisticResponse);

        return ResponseEntity.ok(allResponse);
    }

    @GetMapping("/rosette/monthly")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserRosetteStatistic>> getByRosetteMonthly() {

        List<UserRosetteStatistic> response = userService.getUsersStatisticList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/rosette/weekly")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<UserRosetteStatistic>> getByRosetteWeekly() {

        List<UserRosetteStatistic> response = userService.getUsersStatisticListWeekly();

        return ResponseEntity.ok(response);
    }

}
