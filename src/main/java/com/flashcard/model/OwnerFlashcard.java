package com.flashcard.model;

import com.flashcard.model.enums.YKSLesson;
import com.flashcard.service.OwnerCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "OwnerFlashcard")
@Table(name = "owner_flashcard",
        indexes = {
                @Index(name = "idx_owner_flashcard", columnList = "user_id")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE owner_flashcard SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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

    private boolean deleted = false;

    @OneToMany(mappedBy = "ownerFlashcard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnerCard> ownerCards = new ArrayList<>();
}
