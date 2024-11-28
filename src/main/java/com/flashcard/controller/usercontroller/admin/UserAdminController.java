package com.flashcard.controller.usercontroller.admin;

import com.flashcard.model.DTO.UserDTOAdmin;
import com.flashcard.payload.response.MessageResponse;
import com.flashcard.payload.response.ResponseObject;
import com.flashcard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseObject getUserById(@PathVariable Long id) {
        UserDTOAdmin userDTO = userService.getUserById(id);

        return ResponseObject.ok(userDTO);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAllUsers() {
        List<UserDTOAdmin> allUsers = userService.getAllUsers();

        return ResponseObject.ok(allUsers);
    }

    @GetMapping("/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject getAllUsersByPage(@RequestParam(required = false) String search,
                                            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserDTOAdmin> userDTOPage = userService.getUserPage(search, pageable);

        return ResponseObject.ok(userDTOPage);
    }

    @DeleteMapping("/{userid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject deleteUser(@PathVariable Long userid) {
        userService.deleteUser(userid);

        return ResponseObject.ok(new MessageResponse("Kullanıcı başarıyla silindi"));
    }
}
