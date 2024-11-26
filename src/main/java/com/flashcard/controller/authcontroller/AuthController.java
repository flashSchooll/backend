package com.flashcard.controller.authcontroller;

import com.flashcard.controller.authcontroller.request.LoginRequest;
import com.flashcard.controller.authcontroller.request.SignupRequest;
import com.flashcard.controller.authcontroller.request.UpdatePasswordRequest;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        authService.register(signUpRequest);

        return ResponseEntity.ok(new MessageResponse("Kullanıcı başarıyla kaydedildi!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authService.signIn(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        authService.updatePassword(updatePasswordRequest);

        return ResponseEntity.ok(new MessageResponse("Şifre başarıyle değiştirildi"));
    }

    public void hello(){
        System.out.println("Hello World");
    }
}
