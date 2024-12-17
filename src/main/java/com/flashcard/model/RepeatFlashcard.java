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
@Table(name = "repeat_flashcard")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepeatFlashcard {// tekrar karlarım bilgisi tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    private Topic topic;

    @OneToMany
    @NotNull
    private List<Flashcard> flashcards;

    private LocalDateTime repeatTime;

}
