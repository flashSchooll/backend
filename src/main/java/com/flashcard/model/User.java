package com.flashcard.model;

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
        })
public class User {

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

    @NotBlank
    private Boolean userAgreement;

 /*   @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    @Size(min = 14, max = 14)
    @NotBlank(message = "Please provide your phone number")
    private String phoneNumber;

    @NotNull
    private LocalDate birthdate;

  */

    @NotNull
    private LocalDateTime createdDate;

    private Integer star;

    private Integer rosette;


    private byte[] profilePhoto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public void raiseStar(int addedStar) {
        int star = getStar();

        this.setStar(star + addedStar);
    }

    public void raiseRosette(int addedRosette) {
        int rosette = getRosette();

        this.setStar(rosette + addedRosette);
    }
}
