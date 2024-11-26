package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TYTTopic")
@Table(name = "tyt_topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TYTTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private TYTLesson tytLesson;

    @Size(min = 0,max = 256)
    @NotBlank
    private String subject;
}
