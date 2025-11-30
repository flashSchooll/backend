package com.flashcard.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Duration;

@Entity(name = "UserSeenCard")
@Table(name = "user_seen_card",
        indexes = {
                @Index(name = "idx_user_seen_card_user", columnList = "user"),
                @Index(name = "idx_user_seen_card_user_card", columnList = "user,card")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "user_seen_card_user_card", columnNames = {"user", "card"})
        })
/*@NamedEntityGraph(
        name = "user_seen_card-graph",
        attributeNodes = {
                @NamedAttributeNode("imageData")//todo buraya bakılacak
        }
)*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user_seen_card SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class UserSeenCard {// kullanıcının bitirmiş olduğu kart bilgilerini tutar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Card card;

    private Boolean stateOfKnowledge;

    private Duration duration;

    private boolean deleted = false;
}
