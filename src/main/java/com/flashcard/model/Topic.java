package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "UPDATE topic SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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

    private boolean deleted = false;

    public void updateCardCount(Integer newCardCount) {
        Integer oldCardCount = getCardCount();

        if (newCardCount == null || newCardCount <= 0) {
            throw new IllegalArgumentException("New card count must be greater than zero");
        }

        if (oldCardCount != null && oldCardCount > 0) {
            setCardCount(oldCardCount + newCardCount);
        } else {
            setCardCount(newCardCount);
        }
    }
}
