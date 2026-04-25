package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
@Entity(name = "UserPdfUrl")
@Table(name = "user_pdf_url")
@SQLDelete(sql = "UPDATE user_pdf_url SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class UserPdfUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @NotNull
    private User user;

    @NotNull
    private String url;

    private String subject;

    private boolean deleted = false;
}

  /*

  todo kullanıcının 20 pdf oluşturma olacak  bunun için ayrı bir tablo oluşturulacak

   */