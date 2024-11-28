package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tytlesson.admin.response.TYTLessonResponse;
import com.flashcard.model.TYTLesson;
import com.flashcard.model.enums.TYT;
import com.flashcard.repository.TYTLessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TYTLessonService {

    private final TYTLessonRepository tytLessonRepository;

    @Transactional
    public void save(TYT tyt, MultipartFile icon) throws IOException {

        TYTLesson tytLesson = new TYTLesson();
        tytLesson.setTyt(tyt);
        tytLesson.setIcon(icon.getBytes());

        tytLessonRepository.save(tytLesson);
    }

    @Transactional
    public void delete(Long id) {

        TYTLesson tytLesson = tytLessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        tytLessonRepository.delete(tytLesson);
    }

    public List<TYTLessonResponse> getAll() {

        List<TYTLesson> tytLessons = tytLessonRepository.findAll();

        return tytLessons.stream().map(TYTLessonResponse::new).toList();
    }
}
