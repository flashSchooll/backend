package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "UserFillBlankQuiz")
@Table(name = "user_fill_blank_quiz",
        indexes = {
                @Index(name = "user_fill_blank_quiz_user", columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "user_fill_blank_quiz_title_topic", columnNames = {"user_id", "title", "topic_id"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFillBlankQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotBlank
    private String title;

    @NotNull
    @ManyToOne
    private Topic topic;

}
