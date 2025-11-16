package com.flashcard.service;

import com.flashcard.model.Podcast;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenPodcast;
import com.flashcard.repository.PodcastRepository;
import com.flashcard.repository.UserSeenPodcastRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSeenPodcastService {
    private final UserSeenPodcastRepository userSeenPodcastRepository;
    private final AuthService authService;
    private final PodcastRepository podcastRepository;

    @Transactional
    public void create(Long id) {
        User currentUser = authService.getCurrentUser();
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Podcast bulunamadı"));

        boolean isExists=userSeenPodcastRepository.existsByUserAndPodcastId(currentUser,id);

        if (isExists){
            throw new IllegalArgumentException("Podcast daha önce kaydedilmiş");
        }

        UserSeenPodcast userSeenPodcast = new UserSeenPodcast();
        userSeenPodcast.setId(UUID.randomUUID().toString());
        userSeenPodcast.setUser(currentUser);
        userSeenPodcast.setPodcast(podcast);

        userSeenPodcastRepository.save(userSeenPodcast);
    }

    public List<UserSeenPodcast> findByUser() {
        User currentUser = authService.getCurrentUser();

        return userSeenPodcastRepository.findByUser(currentUser);
    }
}
