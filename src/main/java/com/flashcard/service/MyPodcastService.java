package com.flashcard.service;

import com.flashcard.controller.mypodcast.response.PodcastResponse;
import com.flashcard.model.MyPodcast;
import com.flashcard.model.Podcast;
import com.flashcard.model.User;
import com.flashcard.repository.MyPodcastRepository;
import com.flashcard.repository.PodcastRepository;
import com.flashcard.repository.UserSeenPodcastRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPodcastService {
    private final MyPodcastRepository myPodcastRepository;
    private final AuthService authService;
    private final PodcastRepository podcastRepository;
    private final UserSeenPodcastRepository userSeenPodcastRepository;

    @Transactional
    public void saveForUser(Long podcastId) {
        Objects.requireNonNull(podcastId);
        User user = authService.getCurrentUser();
        Podcast podcast = podcastRepository.findById(podcastId)
                .orElseThrow(() -> new EntityNotFoundException("Podcast not found"));

        Optional<MyPodcast> myPodcast = myPodcastRepository.findByUserAndPodcastId(user, podcastId);

        if (myPodcast.isPresent()) {
            throw new EntityExistsException("Podcast already exists");
        }

        MyPodcast newMyPodcast = MyPodcast.builder().podcast(podcast).user(user).build();

        myPodcastRepository.save(newMyPodcast);
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
        List<Long> seenPodcastList = userSeenPodcastRepository.findIdsByUser(user);

        return podcasts.stream().map(podcast -> {
            boolean isSeen = seenPodcastList.contains(podcast.getId());
            return new PodcastResponse(podcast, isSeen, true);
        }).toList();
    }
}
