package com.flashcard.model;

import com.flashcard.model.enums.Branch;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "User")
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        })
public class User {// kullanıcı bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String userName;

    @NotBlank
    @Size(max = 30)
    private String userSurname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Boolean userAgreement;

    @NotNull
    private LocalDateTime createdDate;

    private Integer star;

    private Integer rosette;

    private byte[] profilePhoto;

    @Enumerated(EnumType.STRING)
    private Branch branch;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public void raiseStar(int addedStar) {
        int alreadyStar = getStar();

        this.setStar(alreadyStar + addedStar);
    }

    public void raiseRosette() {
        int alreadyRosette = getRosette();

        this.setRosette(alreadyRosette + 1);
    }
}
