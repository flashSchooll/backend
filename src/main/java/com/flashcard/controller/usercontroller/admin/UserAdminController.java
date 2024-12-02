package com.flashcard.controller.usercontroller.admin;

import com.flashcard.model.DTO.UserDTOAdmin;
import com.flashcard.model.User;
import com.flashcard.payload.response.MessageResponse;
import com.flashcard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        UserDTOAdmin userDTO = new UserDTOAdmin(user);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserDTOAdmin> allUsers = users.stream().map(UserDTOAdmin::new).toList();

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsersByPage(@RequestParam(required = false) String search,
                                               @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<User> users = userService.getUserPage(search, pageable);

        Page<UserDTOAdmin> userDTOPage = users.map(UserDTOAdmin::new);

        return ResponseEntity.ok(userDTOPage);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.ok(new MessageResponse("Kullanıcı başarıyla silindi"));
    }
}
