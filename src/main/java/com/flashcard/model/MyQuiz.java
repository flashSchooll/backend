package com.flashcard.model;

import com.flashcard.model.enums.QuizOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "MyQuiz")
@Table(name = "my_quiz",
        indexes = {
                @Index(name = "idx_my_quiz_user", columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "my_quiz_user_quiz", columnNames = {"user_id", "quiz_id"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE my_quiz SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class MyQuiz {// Quiz kaydet

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Quiz quiz;

    @Enumerated(value = EnumType.STRING)
    private QuizOption option;

    private boolean deleted = false;
}
