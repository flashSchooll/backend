package com.flashcard.model;

import com.flashcard.model.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Duration;
import java.time.LocalDate;

@Entity(name = "AIQuestionUser")
@Table(name = "ai_question_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE ai_question_user SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class AIQuestionUser {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;

    @ManyToOne
    private AIQuestion aiQuestion;

    @ManyToOne
    private User answeredUser;

    private boolean done;

    private String userAnswer;

    private Duration timeSpent;

    private boolean deleted;

    private LocalDate createdDate;
}
