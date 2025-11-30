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

@Entity(name = "UserQuizAnswer")
@Table(name = "user_quiz_answer",
        indexes = {
                @Index(name = "idx__user_quiz_answer", columnList = "user_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user_quiz_answer SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class UserQuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private QuizOption answer;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Quiz quiz;

    private boolean deleted = false;
}
