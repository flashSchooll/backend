package com.flashcard.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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

}
