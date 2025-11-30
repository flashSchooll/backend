package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "UserSeenPodcast")
@Table(name = "user_seen_podcast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE user_seen_podcast SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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

    private boolean deleted = false;
}
