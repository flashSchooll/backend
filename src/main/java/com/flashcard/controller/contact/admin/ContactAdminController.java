package com.flashcard.controller.contact.admin;

import com.flashcard.constants.Constants;
import com.flashcard.controller.contact.response.ContactResponse;
import com.flashcard.model.Contact;
import com.flashcard.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contact/admin")
@RequiredArgsConstructor
public class ContactAdminController {
    private final ContactService contactService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactResponse>> getAll() {

        List<Contact> contacts = contactService.getAll();

        List<ContactResponse> response = contacts.stream().map(ContactResponse::new).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactResponse> get(@PathVariable Long id) {

        Contact contact = contactService.findById(id);

        ContactResponse response = new ContactResponse(contact);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        contactService.deleteById(id);

        return ResponseEntity.ok(Constants.CONTACT_MESSAGE_SUCCESSFULLY_DELETED);
    }
}
