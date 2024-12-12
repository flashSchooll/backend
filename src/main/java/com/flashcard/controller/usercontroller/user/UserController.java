package com.flashcard.controller.usercontroller.user;

import com.flashcard.controller.usercontroller.user.request.UpdateUserRequest;
import com.flashcard.model.dto.UserDTO;
import com.flashcard.model.User;
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
        User user  = userService.updateUser(updateUserRequest);

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

        userService.saveImage(file);

        return ResponseEntity.ok("Resim başarıyla yüklendi");
    }

    @DeleteMapping("/photo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Object> deletePhoto() {

        userService.deletePhoto();

        return ResponseEntity.ok("Resim başarıyla silindi");
    }

    @GetMapping("/photo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Object> getPhoto() {

        byte[] photo = userService.getImage();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "Content-Disposition", "attachment; filename=\"image.jpg\"").
                contentType(MediaType.IMAGE_JPEG).
                body(photo);
    }
}
