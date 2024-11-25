package com.flashcard.model;

import com.flashcard.model.enums.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "Role")
@Table(name = "role")
@Getter
@Setter
@RequiredArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

}