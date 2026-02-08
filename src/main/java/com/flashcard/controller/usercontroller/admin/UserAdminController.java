package com.flashcard.controller.usercontroller.admin;

import com.flashcard.model.dto.UserDTOAdmin;
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
    public ResponseEntity<UserDTOAdmin> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        UserDTOAdmin userDTO = new UserDTOAdmin(user);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTOAdmin>> getAllUsers(
            @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<UserDTOAdmin> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTOAdmin>> getAllUsersByPage(@RequestParam(required = false) String search,
                                                                @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<User> users = userService.getUserPage(search, pageable);

        Page<UserDTOAdmin> userDTOPage = users.map(UserDTOAdmin::new);

        return ResponseEntity.ok(userDTOPage);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.ok(new MessageResponse("Kullanıcı başarıyla silindi"));
    }

    @PutMapping("/updateRole/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTOAdmin> updateUserRole(@PathVariable Long userId) {
        User user = userService.makeAdmin(userId);
        UserDTOAdmin response = new UserDTOAdmin(user);
        return ResponseEntity.ok(response);
    }
}
