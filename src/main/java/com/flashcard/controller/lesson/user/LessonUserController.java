package com.flashcard.controller.lesson.user;

import com.flashcard.controller.lesson.user.response.TYTLessonCardSeenCountResponse;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.model.enums.YKS;
import com.flashcard.service.UserCardPercentageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lesson/user")
@RequiredArgsConstructor
public class LessonUserController {

    private final UserCardPercentageService userCardPercentageService;

    @GetMapping("/tyt/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TYTLessonCardSeenCountResponse> getTyt() {

        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getAllYks(YKS.TYT);

        return userCardPercentageList.stream().map(TYTLessonCardSeenCountResponse::new).toList();
    }

    @GetMapping("/ayt/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TYTLessonCardSeenCountResponse> getAyt() {

        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getAllYks(YKS.AYT);

        return userCardPercentageList.stream().map(TYTLessonCardSeenCountResponse::new).toList();
    }
}
