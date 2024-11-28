package com.flashcard.security.services;

import com.flashcard.constants.Constants;
import com.flashcard.controller.authcontroller.request.LoginRequest;
import com.flashcard.controller.authcontroller.request.SignupRequest;
import com.flashcard.controller.authcontroller.request.UpdatePasswordRequest;
import com.flashcard.controller.authcontroller.response.JwtResponse;
import com.flashcard.exception.BadRequestException;
import com.flashcard.exception.ResourceNotFoundException;
import com.flashcard.model.Role;
import com.flashcard.model.User;
import com.flashcard.model.enums.ERole;
import com.flashcard.repository.RoleRepository;
import com.flashcard.repository.UserRepository;
import com.flashcard.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(@Valid SignupRequest signUpRequest,MultipartFile file) throws IOException {

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new IllegalArgumentException(String.format(Constants.EMAIL_ALREADY_EXISTS, signUpRequest.getEmail()));
        }

        // Create new user's account
        User user = new User();
        user.setUserName(userNameSaveFormat(signUpRequest.getUserName()));
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setUserSurname(userNameSaveFormat(signUpRequest.getUserSurname()));
        user.setCreatedDate(LocalDateTime.now());
        user.setStar(0);
        user.setRosette(0);
        user.setProfilePhoto(null);
        user.setUserAgreement(signUpRequest.getUserAgreement());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(String.format(Constants.ROLE_NOT_FOUND, ERole.ROLE_USER)));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals(Constants.ROLE_ADMIN)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(String.format(Constants.ROLE_NOT_FOUND, ERole.ROLE_ADMIN)));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(String.format(Constants.ROLE_NOT_FOUND, ERole.ROLE_USER)));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    public JwtResponse signIn(@Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getSurname(),
                userDetails.getEmail(),
                roles);
    }

    @Transactional
    public void updatePassword(@Valid UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(Constants.PASSWORD_NOT_MISMATCH);
        }

        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public User getCurrentUser() {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        return getUserByEmail(email);

    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(Constants.USER_NOT_FOUND));
    }

    public String userNameSaveFormat(String username) {

        String[] words = username.split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return formattedName.toString().trim();
    }
}
