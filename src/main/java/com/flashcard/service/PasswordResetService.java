package com.flashcard.service;

import com.flashcard.model.PasswordResetCode;
import com.flashcard.repository.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;

    public void savePasswordResetToken(PasswordResetCode token){
        passwordResetRepository.save(token);
    }


}