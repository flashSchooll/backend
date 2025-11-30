package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity(name = "RepeatTopic")
@Table(name = "repeat_topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE repeat_topic SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class RepeatTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Topic topic;

    private LocalDateTime repeatTime;

    private boolean deleted = false;
}
