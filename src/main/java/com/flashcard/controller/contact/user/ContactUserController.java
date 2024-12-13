package com.flashcard.controller.contact.user;

import com.flashcard.controller.contact.request.ContactSaveRequest;
import com.flashcard.controller.contact.response.ContactResponse;
import com.flashcard.model.Contact;
import com.flashcard.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contact/user")
@RequiredArgsConstructor
public class ContactUserController {
    private final ContactService contactService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ContactResponse> save(@RequestBody @Valid ContactSaveRequest request) {

        Contact contact = contactService.save(request);

        ContactResponse response = new ContactResponse(contact);

        return ResponseEntity.ok(response);
    }
}
