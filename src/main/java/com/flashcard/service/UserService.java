package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.statistic.response.UserRosetteStatistic;
import com.flashcard.controller.usercontroller.user.request.UpdateUserRequest;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.User;
import com.flashcard.model.dto.UserDTO;
import com.flashcard.model.enums.Branch;
import com.flashcard.repository.UserRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
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

    @Cacheable(value = "users")
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
        user.setBranch(updateUserRequest.getBranch());

        return userRepository.save(user);
    }


    @Transactional
    public void saveImage(MultipartFile file) throws IOException {
        User user = authService.getCurrentUser();

        // Resmi yükle
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        if (inputImage == null) {
            throw new IllegalArgumentException("Invalid image file");
        }

        // Resmi yeniden boyutlandır
        int width = inputImage.getWidth() / 2;
        int height = inputImage.getHeight() / 2;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();

        // Sıkıştır ve kaydet
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new UnsupportedOperationException("JPEG writer not available");
        }
        ImageWriter writer = writers.next();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.7f); // Başlangıç kalitesi
        }

        writer.write(null, new IIOImage(outputImage, null, null), param);
        writer.dispose();
        ios.close();
        byte[] imageData = baos.toByteArray();

        // Dosya boyutunu kontrol et ve kaliteyi azalt
        while (imageData.length > 1.5 * 1024 * 1024) {
            baos.reset();
            param.setCompressionQuality(param.getCompressionQuality() - 0.1f);
            if (param.getCompressionQuality() <= 0.1f) {
                throw new IOException("Cannot reduce image size below 1.5 MB");
            }
            writer.write(null, new IIOImage(outputImage, null, null), param);
            imageData = baos.toByteArray();
        }

        // Kullanıcıya fotoğrafı kaydet
        user.setProfilePhoto(imageData);
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
                    .profilePhoto(u.getProfilePhoto())
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

    @Transactional
    public User updateBranch(Branch branch) {

        User user = authService.getCurrentUser();

        user.setBranch(branch);

        return userRepository.save(user);
    }
}
