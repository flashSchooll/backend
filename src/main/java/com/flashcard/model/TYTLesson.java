package com.flashcard.model;


import com.flashcard.model.enums.TYT;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TYTLesson")
@Table(name = "tyt_lesson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TYTLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TYT tyt;

}
