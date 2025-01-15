package com.flashcard.model;

import com.flashcard.model.enums.QuizOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Quiz")
@Table(name = "quiz")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String question;

    @NotBlank
    private String a;

    @NotBlank
    private String b;

    @NotBlank
    private String c;

    @NotBlank
    private String d;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private QuizOption answer;

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    private Topic topic;
}
