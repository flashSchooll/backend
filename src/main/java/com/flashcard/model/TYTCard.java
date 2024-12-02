package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "TYTCard")
@Table(name = "tyt_card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TYTCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private TYTFlashcard tytFlashcard;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String frontFace;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String backFace;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ImageData> imageData;

}
