package com.flashcard.model;


import com.flashcard.model.enums.YKS;
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
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "daily_target_user_day", columnNames = {"user", "day"})
        })
public class DailyTarget {// günlük hedef bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User user;

    @NotNull
    private LocalDate day;

    @NotNull
    private Integer target;

    @NotNull
    private Integer made;

    @NotNull
    private Integer madeTyt = 0;

    @NotNull
    private Integer madeAyt = 0;


    public void updateMade(int made, YKS yks) {
        if (yks == YKS.TYT) {
            this.madeTyt = getMadeTyt() + made;
        } else if (yks == YKS.AYT) {
            this.madeAyt = getMadeTyt() + made;
        }

        this.made = getMade() + made;
    }


}
