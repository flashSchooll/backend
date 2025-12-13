package com.flashcard.model;


import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "Lesson")
@Table(name = "lesson",
        indexes = {
                @Index(name = "idx_lesson_branch", columnList = "branch"),
                @Index(name = "idx_lesson_yksLesson", columnList = "yks_lesson") // Column name ile birebir eşleşmeli
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE lesson SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "yks_lesson", nullable = false)
    @NotNull
    private YKSLesson yksLesson;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "yks", nullable = false)
    @NotNull
    private YKS yks;

    @NotBlank
    private String path;

    @Column(name = "index_val") // "index" SQL'de rezerve kelimedir! Bunu değiştirmeniz gerekebilir.
    private Long index;

    private boolean deleted = false;
}
