package com.flashcard.service;

import com.flashcard.model.Podcast;
import com.flashcard.model.Topic;
import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.repository.PodcastRepository;
import com.flashcard.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PodcastService {
    private final PodcastRepository podcastRepository;
    private final TopicRepository topicRepository;
    private final S3StorageService s3StorageService;

    @Transactional
    public String savePodcast(MultipartFile file, Long topicId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file boş olamaz");
        }

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        String path = s3StorageService.uploadFile(file, AWSDirectory.PODCAST, topic.getLesson().getYksLesson().name());

        Podcast podcast = new Podcast();
        podcast.setTopic(topic);
        podcast.setPath(path);
        podcastRepository.save(podcast);

        return path;
    }
}
