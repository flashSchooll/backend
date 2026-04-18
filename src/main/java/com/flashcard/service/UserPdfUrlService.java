package com.flashcard.service;

import com.flashcard.controller.userpdfurl.request.UserPdfUrlRequest;
import com.flashcard.controller.userpdfurl.response.UserPdfUrlResponse;
import com.flashcard.model.User;
import com.flashcard.model.UserPdfUrl;
import com.flashcard.repository.UserPdfUrlRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class UserPdfUrlService {
    private final UserPdfUrlRepository userPdfUrlRepository;
    private final AuthService authService;

    @Transactional
    public void save(UserPdfUrlRequest request) {
        User user = authService.getCurrentUser();

        UserPdfUrl userPdfUrl = new UserPdfUrl();
        userPdfUrl.setUser(user);
        userPdfUrl.setUrl(request.getUrl());
        userPdfUrl.setSubject(request.getSubject());
        userPdfUrl.setDeleted(false);

        userPdfUrlRepository.save(userPdfUrl);
    }


    public List<UserPdfUrlResponse> getUserUrls() {
        User user = authService.getCurrentUser();

        List<UserPdfUrl> userPdfUrls = userPdfUrlRepository.findByUser(user);

        return userPdfUrls.stream().map(UserPdfUrlResponse::new).toList();
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = authService.getCurrentUser();

        UserPdfUrl userPdfUrl = userPdfUrlRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new EntityNotFoundException("Url bulunamadı"));

        userPdfUrlRepository.delete(userPdfUrl);

    }
}
