package com.flashcard.controller.quiz.dto;

import java.util.List;

public class QuizGroupDTO {

    /**
     * Grubun ortak prefix'i (seri adı).
     * Örnek: "Fizik_Vektor", "Fizik_TorkDenge"
     */
    private String seriesPrefix;

    /**
     * Bu seriye ait tüm satırlar.
     */
    private List<QuizRowDTO> rows;

    public QuizGroupDTO() {}

    public QuizGroupDTO(String seriesPrefix, List<QuizRowDTO> rows) {
        this.seriesPrefix = seriesPrefix;
        this.rows = rows;
    }

    public String getSeriesPrefix() { return seriesPrefix; }
    public void setSeriesPrefix(String seriesPrefix) { this.seriesPrefix = seriesPrefix; }

    public List<QuizRowDTO> getRows() { return rows; }
    public void setRows(List<QuizRowDTO> rows) { this.rows = rows; }
}