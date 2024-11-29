package com.flashcard.config;

import com.flashcard.model.Role;
import com.flashcard.model.enums.ERole;
import com.flashcard.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Role role1 = Role.builder().id(1).name(ERole.ROLE_ADMIN).build();
        Role role2 = Role.builder().id(2).name(ERole.ROLE_USER).build();

        roleRepository.save(role1);
        roleRepository.save(role2);
    }
}
