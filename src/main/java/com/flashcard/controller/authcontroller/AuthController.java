package com.flashcard.controller.authcontroller;

import com.flashcard.constants.Constants;
import com.flashcard.controller.authcontroller.request.*;
import com.flashcard.controller.authcontroller.response.JwtResponse;
import com.flashcard.payload.response.MessageResponse;
import com.flashcard.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        authService.register(signUpRequest, null);

        return ResponseEntity.ok(Constants.USER_SUCCESSFULLY_SAVED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authService.signIn(loginRequest);

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



}
