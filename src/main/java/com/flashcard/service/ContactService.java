package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.contact.request.ContactSaveRequest;
import com.flashcard.model.ContactMessage;
import com.flashcard.model.User;
import com.flashcard.repository.ContactRepository;
import com.flashcard.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final AuthService authService;

    @Transactional
    public ContactMessage save(@Valid ContactSaveRequest request) {

        User user = authService.getCurrentUser();

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setUser(user);
        contactMessage.setMessage(request.getMessage());
        contactMessage.setTopic(request.getTopic());
        contactMessage.setDeleted(false);

        return contactRepository.save(contactMessage);
    }

 //   @Cacheable(value = "contactMessages")
    public List<ContactMessage> getAll() {
        return contactRepository.findAll();
    }

    public ContactMessage findById(Long id) {
        Objects.requireNonNull(id);

        ContactMessage contactMessage = contactRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CONTACT_MESSAGE_NOT_FOUND));

        contactMessage.updateContact();

        return contactMessage;
    }

    @Transactional
    public void deleteById(Long id) {

        Objects.requireNonNull(id);

        ContactMessage contactMessage = contactRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CONTACT_MESSAGE_NOT_FOUND));

        contactRepository.delete(contactMessage);

    }
}
