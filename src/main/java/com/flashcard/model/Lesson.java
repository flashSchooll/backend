package com.flashcard.model;


import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Lesson")
@Table(name = "lesson")
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

}
