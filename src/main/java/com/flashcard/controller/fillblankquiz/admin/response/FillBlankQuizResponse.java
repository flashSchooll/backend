package com.flashcard.controller.fillblankquiz.admin.response;

import com.flashcard.model.FillBlankQuiz;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Array2DHashSet;

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
        Set<String> letterList = new HashSet<>(Arrays.asList(answer.split("")));

        int size = 14;

        while (letterList.size() < size) {
            char randomHarf = (char) ('a' + rand.nextInt(26));
            letterList.add(String.valueOf(randomHarf));
        }

        List<String> result = new ArrayList<>(letterList);
        Collections.shuffle(result);

        return result;
    }
}
