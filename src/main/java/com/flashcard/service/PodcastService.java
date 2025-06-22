package com.flashcard.service;

import com.flashcard.model.Podcast;
import com.flashcard.model.Topic;
import com.flashcard.model.enums.AWSDirectory;
import com.flashcard.repository.PodcastRepository;
import com.flashcard.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PodcastService {
    private final PodcastRepository podcastRepository;
    private final TopicRepository topicRepository;
    private final S3StorageService s3StorageService;
    private final Logger log = LoggerFactory.getLogger(PodcastService.class);

    @Transactional
    public String savePodcast(MultipartFile file, Long topicId, String title) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("file boş olamaz");
        }

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        String path = s3StorageService.uploadFile(file, AWSDirectory.PODCAST, topic.getLesson().getYksLesson().name());

        Integer duration = getMp3DurationInSeconds(file);

        Podcast podcast = new Podcast();
        podcast.setTopic(topic);
        podcast.setPath(path);
        podcast.setTitle(title);
        podcast.setDuration(duration);

        podcastRepository.save(podcast);

        return path;
    }

    public int getMp3DurationInSeconds(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return 0;
        }

        // MultipartFile'ı geçici bir dosyaya dönüştürme
        File tempFile = null;
        try {
            tempFile = s3StorageService.convertMultipartFileToFile(multipartFile);
            AudioFile audioFile = AudioFileIO.read(tempFile);
            return audioFile.getAudioHeader().getTrackLength(); // Süreyi saniye cinsinden döndürür
        } catch (IOException |
                 CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            log.error("MP3 süresi alınırken hata oluştu: {}", e.getMessage());
            return 0; // Hata durumunda veya okunamadığında 0 veya -1 gibi bir değer dönebilirsiniz
        } finally {
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                if (!deleted) {
                    log.warn("Geçici dosya silinemedi: {}", tempFile.getAbsolutePath());
                }
            }
        }
    }

    public List<Podcast> getByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        return podcastRepository.findByTopicIdAndPublishedTrue(topicId);
    }

    @Transactional
    public void publish(Long podcastId) {
        Objects.requireNonNull(podcastId);

        Podcast podcast = podcastRepository.findById(podcastId)
                .orElseThrow(() -> new EntityNotFoundException("Podcast not found"));
        if (podcast.isPublished()) {
            throw new IllegalArgumentException("Podcast is already published");
        }
        podcast.setPublished(true);

        podcastRepository.save(podcast);
    }

    @Transactional
    public void publishByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        List<Podcast> podcasts = podcastRepository.findByTopicId(topicId);

        podcasts.forEach(podcast -> podcast.setPublished(true));

        podcastRepository.saveAll(podcasts);
    }

    @Transactional
    public void delete(Long podcastId) {
        Objects.requireNonNull(podcastId);

        Podcast podcast = podcastRepository.findById(podcastId)
                .orElseThrow(() -> new EntityNotFoundException("Podcast not found"));

        s3StorageService.deleteFile(podcast.getPath());

        podcastRepository.delete(podcast);
    }

    public Podcast getById(Long podcastId) {
        return podcastRepository.findByIdAndPublishedTrue(podcastId)
                .orElseThrow(() -> new EntityNotFoundException("Podcast not found"));
    }
}
