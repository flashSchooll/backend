package com.flashcard.repository;

public interface EmailSender {
    void send(String to, String email);
}
