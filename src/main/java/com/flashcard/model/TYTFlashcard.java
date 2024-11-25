package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TYTFlashcard")
@Table(name = "tyt_flashcard")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TYTFlashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private TYTTopic topic;

    @NotBlank
    @Size(min = 0, max = 256)
    private String cardName;

}
