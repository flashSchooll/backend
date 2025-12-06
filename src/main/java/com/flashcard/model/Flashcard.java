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

@Entity(name = "Flashcard")
@Table(name = "flashcard",
        indexes = {
                @Index(name = "idx_flashcard_cardName", columnList = "card_name"),
                @Index(name = "idx_flashcard_topic", columnList = "topic_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "flashcard_topic_cardName", columnNames = {"topic_id", "card_name"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE flashcard SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Flashcard {// flashcard bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Topic topic;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String cardName;

    private Integer index;

    private Integer cardCount;

    private Boolean canBePublish;

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
