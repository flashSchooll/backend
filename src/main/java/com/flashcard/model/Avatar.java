package com.flashcard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "Avatar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE avatar SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String path;

    private boolean deleted = false;
}
