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
@Table(name = "daily_target",
        indexes = {
                @Index(name = "idx_daily_target_user", columnList = "user"),
                @Index(name = "idx_daily_target_day", columnList = "day"),
                @Index(name = "idx_daily_target_user_day", columnList = "user,day")
        })
public class DailyTarget {// günlük hedef bilgisini tutar

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


    public void updateMade(int made) {

        this.made = getMade() + made;
    }
}
