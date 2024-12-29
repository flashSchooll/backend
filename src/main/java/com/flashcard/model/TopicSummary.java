package com.flashcard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TopicSummary")
@Table(name = "topic_summary",
        indexes = {
                @Index(name = "idx_topic_topic", columnList = "topic"),
                @Index(name = "idx_topic_summary", columnList = "summary")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicSummary {// konu özetleri bilgisini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Topic topic;

    @NotBlank
    @Size(min = 0, max = 1024)
    private String summary;
}
