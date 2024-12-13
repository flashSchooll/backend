package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.contact.request.ContactSaveRequest;
import com.flashcard.model.Contact;
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
    public Contact save(@Valid ContactSaveRequest request) {

        User user = authService.getCurrentUser();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setMessage(request.getMessage());
        contact.setTopic(request.getTopic());

        return contactRepository.save(contact);
    }

    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    public Contact findById(Long id) {
        Objects.requireNonNull(id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CONTACT_MESSAGE_NOT_FOUND));

        contact.updateContact();

        return contact;
    }

    @Transactional
    public void deleteById(Long id) {

        Objects.requireNonNull(id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.CONTACT_MESSAGE_NOT_FOUND));

        contactRepository.delete(contact);

    }
}
