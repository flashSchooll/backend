package com.flashcard.model;

import com.flashcard.model.enums.QuizOption;
import com.flashcard.model.enums.QuizType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "Quiz")
@Table(name = "quiz")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE quiz SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 512)
    private String question;

    private String a;

    private String b;

    private String c;

    private String d;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private QuizOption answer;

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    private Topic topic;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private QuizType type;

    private boolean deleted = false;
}
