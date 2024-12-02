package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.usercontroller.user.request.UpdateUserRequest;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.DTO.UserDTO;
import com.flashcard.model.DTO.UserDTOAdmin;
import com.flashcard.model.User;
import com.flashcard.repository.UserRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.USER_NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getUserPage(String search, Pageable pageable) {
        return userRepository.findAllAsPage(search, pageable);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    @Transactional
    public void deleteMyAccount() {
        User user = authService.getCurrentUser();

        userRepository.delete(user);
    }

    public UserDTO getUser() {
        User user = authService.getCurrentUser();

        return new UserDTO(user);
    }

    @Transactional
    public User updateUser(UpdateUserRequest updateUserRequest) {
        User user = authService.getCurrentUser();

        if (!user.getEmail().equals(updateUserRequest.getEmail()) && userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new BadRequestException(String.format(Constants.EMAIL_ALREADY_EXISTS, updateUserRequest.getEmail()));
        }

        user.setUserName(authService.userNameSaveFormat(updateUserRequest.getUserName()));
        user.setUserSurname(authService.userNameSaveFormat(updateUserRequest.getUserSurname()));
        user.setEmail(updateUserRequest.getEmail());

        return userRepository.save(user);
    }


    @Transactional
    public void saveImage(MultipartFile file) throws IOException {
        User user = authService.getCurrentUser();

        user.setProfilePhoto(file.getBytes());

        userRepository.save(user);
    }

    public byte[] getImage() {

        User user = authService.getCurrentUser();

        return user.getProfilePhoto();
    }

    @Transactional
    public void deletePhoto() {
        User user = authService.getCurrentUser();
        user.setProfilePhoto(null);

        userRepository.save(user);
    }
}
