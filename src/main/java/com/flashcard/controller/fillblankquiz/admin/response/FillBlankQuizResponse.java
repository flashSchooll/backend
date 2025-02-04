package com.flashcard.controller.fillblankquiz.admin.response;

import com.flashcard.model.FillBlankQuiz;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class FillBlankQuizResponse {
    private static final Random rand = new Random();

    private String question;
    private String answer;
    private List<String> letters;

    public FillBlankQuizResponse(FillBlankQuiz fillBlankQuiz) {
        this.question = fillBlankQuiz.getQuestion();
        this.answer = fillBlankQuiz.getAnswer();
        this.letters = getLetters(answer);
    }

    private List<String> getLetters(String answer) {

        List<String> letterList = new ArrayList<>(Arrays.stream(answer.split("")).toList());

        int size = answer.length() < 10 ? 10 : 20;

        while (letterList.size() < size) {

            char randomHarf = (char) ('a' + rand.nextInt(26));
            letterList.add(String.valueOf(randomHarf));
        }
        Collections.shuffle(letterList);

        return letterList;
    }
}
