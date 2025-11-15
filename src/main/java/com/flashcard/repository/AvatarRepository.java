package com.flashcard.repository;

import com.flashcard.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar,String> {
}
