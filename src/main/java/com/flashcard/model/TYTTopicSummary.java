package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TYTTopicSummary")
@Table(name = "tyt_topic_summary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TYTTopicSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private TYTTopic topic;

    @NotBlank
    @Size(min = 0,max = 1024)
    private String summary;
}
