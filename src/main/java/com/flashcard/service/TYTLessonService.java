package com.flashcard.service;

import com.flashcard.controller.tytlesson.admin.request.TYTLessonSaveRequest;
import com.flashcard.controller.tytlesson.admin.response.TYTLessonResponse;
import com.flashcard.model.TYTLesson;
import com.flashcard.repository.TYTLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TYTLessonService {

    private final TYTLessonRepository tytLessonRepository;

    @Transactional
    public void save(TYTLessonSaveRequest tytLessonSaveRequest) {

        TYTLesson tytLesson = new TYTLesson();
        tytLesson.setTyt(tytLessonSaveRequest.getTyt());

        tytLessonRepository.save(tytLesson);
    }

    @Transactional
    public void delete(Long id) {
        TYTLesson tytLesson = tytLessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ders bulunamadı"));

        tytLessonRepository.delete(tytLesson);
    }

    public List<TYTLessonResponse> getAll() {

        List<TYTLesson> tytLessons = tytLessonRepository.findAll();

        return tytLessons.stream().map(TYTLessonResponse::new).toList();
    }
}
