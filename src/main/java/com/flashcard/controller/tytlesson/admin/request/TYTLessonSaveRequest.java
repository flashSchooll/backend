package com.flashcard.controller.tytlesson.admin.request;

import com.flashcard.model.enums.TYT;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TYTLessonSaveRequest {

    @NotNull
    private TYT tyt;

    @NotNull
    private MultipartFile icon;
}
