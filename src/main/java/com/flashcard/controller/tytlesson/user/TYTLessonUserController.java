package com.flashcard.controller.tytlesson.user;

import com.flashcard.controller.tytlesson.user.response.TYTLessonCardSeenCountResponse;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.service.UserCardPercentageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tyt/lesson/user")
@RequiredArgsConstructor
public class TYTLessonUserController {

    private final UserCardPercentageService userCardPercentageService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TYTLessonCardSeenCountResponse> getId() {

        List<UserCardPercentage> userCardPercentageList = userCardPercentageService.getAllTYT();

        return userCardPercentageList.stream().map(TYTLessonCardSeenCountResponse::new).toList();
    }
}
