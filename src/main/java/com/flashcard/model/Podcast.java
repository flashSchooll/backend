package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "Podcast")
@Table(name = "podcast")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE podcast SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Topic topic;

    @NotBlank
    private String path;

    @NotBlank
    private String photoPath;

    @NotNull
    private String title;

    @NotNull
    private Integer duration;

    private boolean published = false;

    private boolean deleted = false;
}
