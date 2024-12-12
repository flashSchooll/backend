package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Topic")
@Table(name = "topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {// derslere ait konu bilgilerini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Lesson lesson;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String subject;
}
