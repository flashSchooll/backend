package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity(name = "ErrorSupport")
@Table(name = "error_support")
@Getter
@Setter
@SQLDelete(sql = "UPDATE error_support SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ErrorSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String errorMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Card card;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User user;

    private Boolean solved = false;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime solvedDate;

    private boolean deleted = false;
}
