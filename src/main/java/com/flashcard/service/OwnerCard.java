package com.flashcard.service;

import com.flashcard.model.OwnerFlashcard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "OwnerCard")
@Table(name = "owner_card",
        indexes = {
                @Index(name = "idx_owner_card_owner_flashcard", columnList = "owner_flashcard_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OwnerFlashcard ownerFlashcard;

    @NotBlank
    @Size(max = 512)
    private String frontFace;

    @NotBlank
    @Size(max = 512)
    private String backFace;

    private LocalDateTime createdDate;
}
