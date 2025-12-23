package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity(name = "AIQuestion")
@Table(name = "ai_question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE ai_question SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class AIQuestion {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Topic topic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "generate_user")
    private User generateUser;

    private String uuid; // bunu gelen soruları gruplamak için kullanabiliriz

    private String subject;

    private String question;

    private String answer;

    private String a;

    private String b;

    private String c;

    private String d;

    private String e;

    private String level;

    @Length(max = 1024)
    private String description;

    private boolean deleted = false;

    private boolean published = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
