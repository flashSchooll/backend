package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Topic")
@Table(name = "topic",
        indexes = {
                @Index(name = "idx_topic_lesson", columnList = "lesson_id"),
                @Index(name = "idx_topic_subject", columnList = "subject")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {// derslere ait konu bilgilerini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Lesson lesson;

    // @Column(columnDefinition = "TEXT")
    @NotBlank
    private String subject;

    private Integer index;

    private Integer cardCount;

    public void updateCardCount(Integer newCardCount) {
        int oldCardCount = getCardCount();

        if (newCardCount != null && newCardCount > 0) {
            setCardCount(oldCardCount + newCardCount);
        }
    }
}
