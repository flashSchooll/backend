package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "Podcast")
@Table(name = "podcast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Topic topic;

    @NotBlank
    private String path;

    @NotNull
    private String title;

}
