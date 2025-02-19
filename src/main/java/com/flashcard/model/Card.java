package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "Card")
@Table(name = "card",
        indexes = {
                @Index(name = "idx_card_flashcard", columnList = "flashcard")
        })
@Getter
@Setter
@NamedEntityGraph(
        name = "card-graph",
        attributeNodes = {
                @NamedAttributeNode("imageData")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class Card {// kart bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Flashcard flashcard;

    @Column(columnDefinition = "TEXT")
    private String frontFace;

    @Column(columnDefinition = "TEXT")
    private String backFace;

    @Size(max = 128)
    private String frontPhotoPath;

    @Size(max = 128)
    private String backPhotoPath;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ImageData> imageData;

}
