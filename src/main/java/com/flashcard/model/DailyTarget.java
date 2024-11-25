package com.flashcard.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "DailyTarget")
@Table(name = "daily_target")
public class DailyTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @NotNull
    private LocalDate day;

    @NotNull
    private Integer target;

    @NotNull
    private Integer made;
}
