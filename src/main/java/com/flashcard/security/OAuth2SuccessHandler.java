package com.flashcard.security;

import com.flashcard.model.User;
import com.flashcard.model.enums.Provider;
import com.flashcard.repository.UserRepository;
import com.flashcard.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;          // kendi utility sınıfınız
    private final UserRepository userRepo;    // JPA repo

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName  = oAuth2User.getAttribute("family_name");
        if (lastName == null) lastName = "";

        String finalLastName = lastName;
        User user = userRepo.findByEmail(email)
                .orElseGet(() -> userRepo.save(
                        User.builder()
                                .email(email)
                                .userName(firstName)      // @NotBlank alan
                                .userSurname(finalLastName)
                                .series(1)
                                .branch(null)
                                .target(100)
                                .star(0)
                                .weeklyStar(0)
                                .rosette(0)
                                .userAgreement(false) // todo buraya bakılacak
                                .createdDate(LocalDateTime.now())
                                .password("") // sosyal login → boş bırak
                                .provider(Provider.GOOGLE) // kendi enum’unuz
                                .build()));

        String token = jwtUtils.generateJwtToken(authentication); // kendi metodunuz

        response.addHeader("Authorization", "Bearer " + token);

        getRedirectStrategy().sendRedirect(request, response,
                "http://localhost:3000/oauth-success?token=" + token);
    }
}