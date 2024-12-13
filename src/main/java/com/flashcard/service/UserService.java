package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.statistic.response.UserRosetteStatistic;
import com.flashcard.controller.usercontroller.user.request.UpdateUserRequest;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.dto.UserDTO;
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
import java.util.*;

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

        boolean existsEmail = userRepository.existsByEmail(updateUserRequest.getEmail());

        if (!user.getEmail().equals(updateUserRequest.getEmail()) && existsEmail) {
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

    public List<UserRosetteStatistic> getUsersStatisticList() {

        User user = authService.getCurrentUser();

        List<User> users = userRepository.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(User::getRosette)
                                .reversed()
                                .thenComparing(Comparator.comparing(User::getStar).reversed())
                )
                .toList();

        List<UserRosetteStatistic> statistics = new ArrayList<>();

        users.forEach(u -> {
            UserRosetteStatistic userStatistic = UserRosetteStatistic.builder()
                    .userName(u.getUserName())
                    .userSurname(u.getUserSurname())
                    .id(u.getId())
                    .star(u.getStar())
                    .rosette(u.getRosette())
                    .order(statistics.size())
                    .me(Objects.equals(u.getId(), user.getId()))
                    .build();

            statistics.add(userStatistic);
        });

        return statistics;
    }
}
