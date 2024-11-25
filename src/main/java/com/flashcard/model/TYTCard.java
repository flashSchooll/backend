package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Size(min = 0, max = 256)
    @NotBlank
    private String frontFace;

    @Size(min = 0, max = 256)
    @NotBlank
    private String backFace;

    private byte[] dataFrontFace;

    private byte[] dataBackFace;
}
