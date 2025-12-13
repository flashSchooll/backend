package com.flashcard.model;

import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        })
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class User {// kullanıcı bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String userName;

    @Size(max = 30)
    private String userSurname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider; // google, facebook gibi sosyal girişler için

    @NotNull
    private Boolean userAgreement = false;

    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    private Integer star = 0;

    private Integer weeklyStar = 0;

    private Integer rosette = 0;

    @Enumerated(EnumType.STRING)
    private Branch branch;

    private Integer target = 100;

    private String photoPath;

    private Integer series = 1; // todo seri kırılınca sıfır olmalı bakılacak

    private Integer targetSeries = 0;

    private boolean deleted = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactMessage> contactMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSeenCard> userSeenCards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCardPercentage> userCardPercentages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepeatFlashcard> repeatFlashcards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyTarget> dailyTargets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthDailyTarget> monthDailyTargets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyCard> myCards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordResetCode> passwordResetCodes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFillBlankQuiz> userFillBlankQuizs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnerFlashcard> ownerFlashcards = new ArrayList<>();


    public void raiseStar(int addedStar) {
        Integer alreadyStar = getStar();
        Integer alreadyWeeklyStar = getWeeklyStar();

        this.setStar(alreadyStar + addedStar);
        this.setWeeklyStar(alreadyWeeklyStar + addedStar);
    }

    public void raiseRosette() {
        Integer alreadyRosette = getRosette();

        this.setRosette(alreadyRosette + 1);
    }

    public String getUserNameAndSurname() {
        return userName + " " + userSurname;
    }

    public void updateSeriesCount() {
        this.series = this.series + 1;
    }
}
