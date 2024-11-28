package com.flashcard.controller.authcontroller;

import com.flashcard.constants.Constants;
import com.flashcard.controller.authcontroller.request.LoginRequest;
import com.flashcard.controller.authcontroller.request.SignupRequest;
import com.flashcard.controller.authcontroller.request.UpdatePasswordRequest;
import com.flashcard.controller.authcontroller.response.JwtResponse;
import com.flashcard.payload.response.MessageResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseObject registerUser(@Valid @RequestBody SignupRequest signUpRequest ,
                                       @RequestParam("file") MultipartFile file) throws IOException {

        authService.register(signUpRequest,file);

        return ResponseObject.ok(Constants.USER_SUCCESSFULLY_SAVED);
    }

    @PostMapping("/signin")
    public ResponseObject authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authService.signIn(loginRequest);

        return ResponseObject.ok(jwtResponse);
    }

    @PutMapping("/update-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseObject updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        authService.updatePassword(updatePasswordRequest);

        return ResponseObject.ok(new MessageResponse(Constants.PASSWORD_SUCCESSFULLY_UPDATED));
    }

}
