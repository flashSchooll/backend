package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "UserSeenPodcast")
@Table(name = "user_seen_podcast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSeenPodcast { // kullanıcının dinlediği podcastler

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Podcast podcast;
}
