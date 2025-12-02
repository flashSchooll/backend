package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.topicsummary.request.TopicSummarySaveRequest;
import com.flashcard.controller.topicsummary.request.TopicSummaryUpdateRequest;
import com.flashcard.controller.topicsummary.response.TopicSummaryResponse;
import com.flashcard.model.Topic;
import com.flashcard.model.TopicSummary;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.TopicSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TopicSummaryService {

    private final TopicSummaryRepository topicSummaryRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public TopicSummary save(TopicSummarySaveRequest request) {
        Objects.requireNonNull(request.getTopicId());
        Objects.requireNonNull(request.getSummary());

        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        TopicSummary summary = new TopicSummary();
        summary.setTopic(topic);
        summary.setSummary(request.getSummary());
        summary.setDeleted(false);

        return topicSummaryRepository.save(summary);
    }

    @Transactional
    public TopicSummary update(TopicSummaryUpdateRequest request) {
        Objects.requireNonNull(request.getSummaryId());
        Objects.requireNonNull(request.getSummary());

        TopicSummary summary = topicSummaryRepository.findById(request.getSummaryId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_SUMMARY_NOT_FOUND));

        summary.setSummary(request.getSummary());

        return topicSummaryRepository.save(summary);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        TopicSummary summary = topicSummaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_SUMMARY_NOT_FOUND));

        topicSummaryRepository.delete(summary);
    }

    @Cacheable(value = "topicSummary", key = "#summaryId")
    public TopicSummary get(Long summaryId) {
        Objects.requireNonNull(summaryId);

        return topicSummaryRepository.findById(summaryId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_SUMMARY_NOT_FOUND));
    }

  //  @Cacheable(value = "topicSummaries", key = "#topicId")
    public List<TopicSummaryResponse> getAllByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        List<TopicSummary> summaries = topicSummaryRepository.findByTopic(topic);

        return summaries.stream().map(TopicSummaryResponse::new).toList();
    }

   // @Cacheable(value = "topicSummaries", key = "#topicId")
    public Page<TopicSummaryResponse> getAllByTopic(Pageable pageable,Long topicId) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        Page<TopicSummary> summaries = topicSummaryRepository.findByTopic(topic,pageable);

        return summaries.map(TopicSummaryResponse::new);
    }

    public Page<TopicSummaryResponse> getAll(Pageable pageable) {
        return topicSummaryRepository.findAll(pageable).map(TopicSummaryResponse::new);
    }
}
