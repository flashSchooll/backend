package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.Lesson;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import com.flashcard.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    @Transactional
    public Lesson save(YKS yks, Branch branch, YKSLesson yksLesson, MultipartFile icon) throws IOException {

        Optional<Lesson> optionalLesson = lessonRepository.findByYksAndBranchAndYksLesson(yks, branch, yksLesson);

        if (optionalLesson.isPresent()) {
            throw new BadRequestException(Constants.LESSON_ALREADY_SAVED);
        }

        Lesson lesson = new Lesson();
        lesson.setYksLesson(yksLesson);
        lesson.setBranch(branch);
        lesson.setYks(yks);
        lesson.setIcon(icon.getBytes());

        return lessonRepository.save(lesson);
    }

    @Transactional
    public void delete(Long id) {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        lessonRepository.delete(lesson);
    }

    public List<Lesson> getAll() {

        return lessonRepository.findAll();
    }

    public Lesson update(Long id, YKSLesson yksLesson, Branch branch, YKS yks, MultipartFile icon) throws IOException {

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        lesson.setYksLesson(yksLesson);
        lesson.setBranch(branch);
        lesson.setYks(yks);

        if (icon != null) {
            lesson.setIcon(icon.getBytes());
        }

        return lessonRepository.save(lesson);
    }
}
