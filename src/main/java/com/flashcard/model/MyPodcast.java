package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "MyPodcast")
@Table(name = "my_podcast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPodcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Podcast podcast;

}
