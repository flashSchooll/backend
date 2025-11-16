package com.flashcard.service;

import com.flashcard.controller.mypodcast.response.PodcastResponse;
import com.flashcard.model.MyPodcast;
import com.flashcard.model.Podcast;
import com.flashcard.model.User;
import com.flashcard.repository.MyPodcastRepository;
import com.flashcard.repository.PodcastRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MyPodcastService {
    private final MyPodcastRepository myPodcastRepository;
    private final AuthService authService;
    private final PodcastRepository podcastRepository;

    @Transactional
    public void saveForUser(Long podcastId) {
        Objects.requireNonNull(podcastId);
        User user = authService.getCurrentUser();
        Podcast podcast = podcastRepository.findById(podcastId)
                .orElseThrow(() -> new EntityNotFoundException("Podcast not found"));

        MyPodcast myPodcast = MyPodcast.builder().podcast(podcast).user(user).build();

        myPodcastRepository.save(myPodcast);
    }

    @Transactional
    public void delete(Long podcastId) {
        Objects.requireNonNull(podcastId);
        User user = authService.getCurrentUser();

        MyPodcast myPodcast = myPodcastRepository.findByUserAndPodcastId(user, podcastId)
                .orElseThrow(() -> new EntityNotFoundException("Podcast kayıtlı podcast bulunamadı"));

        myPodcastRepository.delete(myPodcast);
    }

    public List<PodcastResponse> getMyPodcasts() {
        User user = authService.getCurrentUser();

        List<Podcast> podcasts = myPodcastRepository.findByUser(user);

        return podcasts.stream().map(PodcastResponse::new).toList();
    }
}
