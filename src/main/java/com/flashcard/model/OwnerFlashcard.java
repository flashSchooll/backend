package com.flashcard.model;

import com.flashcard.model.enums.YKSLesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "OwnerFlashcard")
@Table(name = "owner_flashcard",
        indexes = {
                @Index(name = "idx_owner_flashcard", columnList = "user_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerFlashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private YKSLesson lesson;

    @NotNull
    @ManyToOne
    private User user;
}
