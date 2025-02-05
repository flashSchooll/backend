package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @ManyToOne
    @NotNull
    private Flashcard flashcard;

    @Column(columnDefinition = "TEXT")
    @NotBlank                    // todo boş olabilecek şeklide güncellenecek
    private String frontFace;

    @Column(columnDefinition = "TEXT")
    @NotBlank                   // todo boş olabilecek şeklide güncellenecek
    private String backFace;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ImageData> imageData;

}
