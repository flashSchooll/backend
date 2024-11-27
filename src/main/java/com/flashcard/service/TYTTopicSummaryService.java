package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tyttopicsummary.request.TYTTopicSummarySaveRequest;
import com.flashcard.controller.tyttopicsummary.request.TYTTopicSummaryUpdateRequest;
import com.flashcard.controller.tyttopicsummary.response.TYTTopicSummaryResponse;
import com.flashcard.model.TYTTopic;
import com.flashcard.model.TYTTopicSummary;
import com.flashcard.repository.TYTTopicRepository;
import com.flashcard.repository.TYTTopicSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TYTTopicSummaryService {

    private final TYTTopicSummaryRepository tytTopicSummaryRepository;
    private final TYTTopicRepository tytTopicRepository;

    @Transactional
    public void save(TYTTopicSummarySaveRequest request) {
        Objects.requireNonNull(request.getTopicId());
        Objects.requireNonNull(request.getSummary());

        TYTTopic topic = tytTopicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        TYTTopicSummary summary = new TYTTopicSummary();
        summary.setTopic(topic);
        summary.setSummary(request.getSummary());

        tytTopicSummaryRepository.save(summary);
    }

    @Transactional
    public void update(TYTTopicSummaryUpdateRequest request) {
        Objects.requireNonNull(request.getSummaryId());
        Objects.requireNonNull(request.getSummary());

        TYTTopicSummary summary = tytTopicSummaryRepository.findById(request.getSummaryId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_SUMMARY_NOT_FOUND));

        summary.setSummary(request.getSummary());

        tytTopicSummaryRepository.save(summary);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        TYTTopicSummary summary = tytTopicSummaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_SUMMARY_NOT_FOUND));

        tytTopicSummaryRepository.delete(summary);
    }

    public TYTTopicSummaryResponse get(Long id) {
        Objects.requireNonNull(id);

        TYTTopicSummary summary = tytTopicSummaryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_SUMMARY_NOT_FOUND));

        return new TYTTopicSummaryResponse(summary);
    }

    public List<TYTTopicSummaryResponse> getAllByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        TYTTopic topic = tytTopicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        List<TYTTopicSummary> summaries = tytTopicSummaryRepository.findByTopic(topic);

        return summaries.stream().map(TYTTopicSummaryResponse::new).toList();
    }
}
