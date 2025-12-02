package com.flashcard.controller.authcontroller;

import com.flashcard.constants.Constants;
import com.flashcard.controller.authcontroller.request.*;
import com.flashcard.controller.authcontroller.response.JwtResponse;
import com.flashcard.model.User;
import com.flashcard.model.enums.Branch;
import com.flashcard.payload.response.MessageResponse;
import com.flashcard.security.services.AuthService;
import com.flashcard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import java.io.IOException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestPart(required = false) MultipartFile photo,
            @Valid @RequestPart SignupRequest signUpRequest
    ) throws IOException {

        authService.register(signUpRequest, photo);

        return ResponseEntity.ok(Constants.USER_SUCCESSFULLY_SAVED);
    }

    // @PostMapping("/signin")
    // public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
    //     JwtResponse jwtResponse = authService.signIn(loginRequest);
//
    //     return ResponseEntity.ok(jwtResponse);
    // }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {  // todo daha sonra kaldırılacak

        JwtResponse jwtResponse = authService.signIn(loginRequest);
        User user = userService.getUserById(jwtResponse.getId());
        jwtResponse.setStar(user.getStar());
        jwtResponse.setRosette(user.getRosette());
        jwtResponse.setBranch(user.getBranch());
        jwtResponse.setSeries(user.getSeries());
        jwtResponse.setPhotoPath(user.getPhotoPath());
        jwtResponse.setUsername(user.getUserName());
        jwtResponse.setTargetSeries(user.getTargetSeries());
        return ResponseEntity.ok(jwtResponse);
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        authService.updatePassword(updatePasswordRequest);

        return ResponseEntity.ok(new MessageResponse(Constants.PASSWORD_SUCCESSFULLY_UPDATED));
    }

    @PutMapping("/forgot-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {

        authService.forgotPassword(forgotPasswordRequest);

        return ResponseEntity.ok("Mail başarıyla gönderildi");
    }

    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        Boolean reset = authService.resetPassword(resetPasswordRequest);

        return ResponseEntity.ok(reset);
    }

    @PutMapping("/new-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> newPassword(@Valid @RequestBody NewPasswordRequest newPasswordRequest) {

        authService.newPassword(newPasswordRequest);

        return ResponseEntity.ok("Şifre başarıyla oluşturuldu");
    }


    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        return "user";
    }
}
