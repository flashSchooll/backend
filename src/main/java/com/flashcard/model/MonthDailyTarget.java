package com.flashcard.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Month;

@Entity(name = "MonthDailyTarget")
@Table(name = "month_daily_target",
        indexes = {
                @Index(name = "idx_month_daily_target_user_month", columnList = "user,month")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "month_daily_target_user_month", columnNames = {"user", "month"})
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthDailyTarget {// geçmiş aylara yönelik günlük hedef bilgisini ay ortalaması olarak tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @NotNull
    private Month month;

    @NotNull
    private Integer average;
}
