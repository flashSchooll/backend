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

@Entity(name = "Lesson")
@Table(name = "lesson",
        indexes = {
                @Index(name = "idx_lesson_branch", columnList = "branch"),
                @Index(name = "idx_lesson_yksLesson", columnList = "yksLesson")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {//ders bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @NotNull()
    private YKSLesson yksLesson;

    @Enumerated(value = EnumType.STRING)
    private Branch branch;

    @Enumerated(value = EnumType.STRING)
    @NotNull()
    private YKS yks;

    @NotNull
    private byte[] icon;

    @NotBlank
    private String path;

}
