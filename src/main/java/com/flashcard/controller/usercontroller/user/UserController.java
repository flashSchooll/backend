package com.flashcard.controller.usercontroller.user;

import com.flashcard.controller.usercontroller.user.request.UpdateUserRequest;
import com.flashcard.model.User;
import com.flashcard.model.dto.UserDTO;
import com.flashcard.model.enums.Branch;
import com.flashcard.payload.response.MessageResponse;
import com.flashcard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> get() {
        UserDTO userDTO = userService.getUser();

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> update(@RequestBody UpdateUserRequest updateUserRequest) {
        User user = userService.updateUser(updateUserRequest);

        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteMyAccount() {
        userService.deleteMyAccount();

        return ResponseEntity.ok(new MessageResponse("Hesabınız başarıyla silindi"));
    }

    @PostMapping("/upload-photo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Object> uploadPhoto(@RequestBody MultipartFile file) throws IOException {

        String path = userService.saveImage(file);

        return ResponseEntity.ok(path);
    }

    @DeleteMapping("/photo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Object> deletePhoto() {

        userService.deletePhoto();

        return ResponseEntity.ok("Resim başarıyla silindi");
    }

    @PutMapping("/update-branch")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateBranch(@RequestParam Branch branch) {
        User user = userService.updateBranch(branch);

        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update-target")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateTarget(@RequestParam Integer target) {
        User user = userService.updateTarget(target);

        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update-target-series")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateTargetSeries(@RequestParam Integer target) {
        User user = userService.updateTargetSeries(target);

        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update-star-rosette")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateStarAntRosette(@RequestParam Integer star) {
        User user = userService.updateStarAndRosette(star);

        UserDTO userDTO = new UserDTO(user);

        return ResponseEntity.ok(userDTO);
    }


}
