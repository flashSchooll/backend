package com.flashcard.controller.lesson.user;

import com.flashcard.controller.lesson.user.response.LessonListResponse;
import com.flashcard.controller.lesson.user.response.TYTLessonCardSeenCountResponse;
import com.flashcard.model.Lesson;
import com.flashcard.model.User;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.LessonService;
import com.flashcard.service.UserCardPercentageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lesson/user")
@RequiredArgsConstructor
public class LessonUserController {

    private final UserCardPercentageService userCardPercentageService;
    private final AuthService authService;
    private final LessonService lessonService;

    @GetMapping("/tyt/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TYTLessonCardSeenCountResponse> getTyt() {
        User user = authService.getCurrentUser();
        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getTyt(user);

        return userCardPercentageList.stream()
                .sorted(Comparator.comparing(u -> u.getLesson().getIndex(), Comparator.nullsLast(Long::compareTo)))
                .map(TYTLessonCardSeenCountResponse::new)
                .toList();
    }

    @GetMapping("/ayt/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TYTLessonCardSeenCountResponse> getAyt() {
        User user = authService.getCurrentUser();
        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getAyt(user);

        return userCardPercentageList.stream().map(TYTLessonCardSeenCountResponse::new).toList();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<LessonListResponse> getLessonList(){
        List<Lesson> lessons=lessonService.getAll();

        return lessons.stream().map(LessonListResponse::new).toList();
    }


}
