package com.flashcard.controller.fillblankquiz.admin.response;

import com.flashcard.model.FillBlankQuiz;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class FillBlankQuizResponse {
    private static final Random rand = new Random();

    private String question;
    private String answer;
    private List<String> letters;

    public FillBlankQuizResponse(FillBlankQuiz fillBlankQuiz) {
        this.question = fillBlankQuiz.getQuestion().trim().toUpperCase();
        this.answer = fillBlankQuiz.getAnswer().trim().toUpperCase();
        this.letters = getLetters(answer);
    }

    private List<String> getLetters(String answer) {
        Set<String> letterList = new HashSet<>(Arrays.asList(answer.split(""))).stream().toList().stream().map(String::toUpperCase).collect(Collectors.toSet());

        int size = 14;

        while (letterList.size() < size) {
            char randomHarf = (char) ('a' + rand.nextInt(26));
            letterList.add(String.valueOf(randomHarf).toUpperCase());
        }

        List<String> result = new ArrayList<>(letterList);
        Collections.shuffle(result);

        return result;
    }
}
