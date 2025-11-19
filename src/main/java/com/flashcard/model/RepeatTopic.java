package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "RepeatTopic")
@Table(name = "repeat_topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
