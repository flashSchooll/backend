package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "FillBlankQuiz")
@Table(name = "fill_blank_quiz",
        indexes = {
                @Index(name = "idx_fill_blank_quiz_topic", columnList = "topic_id"),
                @Index(name = "idx_fill_blank_quiz_title", columnList = "title")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "fill_blank_quiz_topic_title_question", columnNames = {"topic_id", "title","question"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FillBlankQuiz {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    @NotBlank
    private String title;

    @NotNull
    @ManyToOne
    private Topic topic;
}
