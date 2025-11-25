package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "RepeatFlashcard")
@Table(name = "repeat_flashcard",
        indexes = {
                @Index(name = "idx_repeat_flashcard_user", columnList = "user"),
                @Index(name = "idx_repeat_flashcard_topic", columnList = "topic")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepeatFlashcard {// tekrar kartlarım bilgisi tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Flashcard flashcard;

    private LocalDateTime repeatTime;

}
