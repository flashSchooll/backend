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
@Entity(name = "UserSeries")
@Table(name = "user_series")
public class UserSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @NotNull
    private LocalDate date;
}
