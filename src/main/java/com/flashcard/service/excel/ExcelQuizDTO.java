package com.flashcard.service.excel;

import com.flashcard.model.enums.QuizOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelQuizDTO {
    private String question;
    private String a;
    private String b;
    private String c;
    private String d;
    private QuizOption answer;
    private String name;
}
