package com.flashcard.security;

import com.flashcard.security.jwt.AuthEntryPointJwt;
import com.flashcard.security.jwt.AuthTokenFilter;
import com.flashcard.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /* 1) CORS & CSRF */
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                /* 2) Hataları yakala */
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))

                /* 3) JWT ile stateless çalışmaya devam */
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /* 4) Yetkilendirme */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/test/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/favicon.ico",
                                "/oauth2/**",
                                "/login/**",          // <--- EKLEME: Login ve hata sayfasına izin ver
                                "/login/oauth2/code/**"
                        ).permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                /* 5) OAuth2 Login */
                .oauth2Login(oauth2 -> oauth2
                        // Giriş başlatma URL'i (Default: /oauth2/authorization/google)
                        // Redirect URL'i (Default: /login/oauth2/code/google)
                        .successHandler(oAuth2SuccessHandler)
                        .failureUrl("/login?error=true")
                );

        /* 6) Filtreler */
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

   /* @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns( // Değişiklik burada
                                "https://flazzyapp.com",
                                "https://www.flazzyapp.com",
                                "http://localhost:3000",
                                "http://localhost:5173"
                        )
                        .allowedHeaders("*")
                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowCredentials(false);
            }
        };
    }


    protected void configure(WebSecurity web) {
        web.ignoring().requestMatchers(
                "/favicon.ico",
                "/resources/**",
                "/static/**",
                "/public/**",
                "/webjars/**");
    }


    */
   @Bean
   CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();

       configuration.setAllowedOriginPatterns(Arrays.asList(
               "https://flazzyapp.com",
               "https://www.flazzyapp.com",
               "http://localhost:3000",
               "http://localhost:5173"
       ));

       configuration.setAllowedMethods(Arrays.asList(
               "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
       ));

       configuration.setAllowedHeaders(Arrays.asList(
               "Authorization",
               "Content-Type",
               "Accept"
       ));

       configuration.setExposedHeaders(Arrays.asList("Authorization"));

       configuration.setAllowCredentials(false); // 🔥 JWT header kullanıyorsun

       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", configuration);
       return source;
   }


}
