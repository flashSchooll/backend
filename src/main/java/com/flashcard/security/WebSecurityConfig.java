package com.flashcard.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.flashcard.security.jwt.AuthEntryPointJwt;
import com.flashcard.security.jwt.AuthTokenFilter;
import com.flashcard.security.services.UserDetailsServiceImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

// @Override
// public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//   authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
// }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

 /* @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            // Swagger UI'yi güvenlikten hariç tutuyoruz
            .anyRequest("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Swagger UI ve API dokümanlarını herkese açık hale getiriyoruz
            .anyRequest().authenticated()  // Diğer tüm isteklere kimlik doğrulama gerektiriyor
            .and()
            .csrf().disable();

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // burada unauthorizedHandler AuthEntryPointJwt
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()  // /api/auth/** endpoint'lerine erişim izin veriliyor.
                                .requestMatchers("/api/test/**").permitAll()  // /api/test/** endpoint'lerine erişim izin veriliyor.
                                //    .requestMatchers("swagger-ui/**").permitAll()  // /api/test/** endpoint'lerine erişim izin veriliyor.
                                .anyRequest().authenticated()  // Diğer tüm endpoint'ler kimlik doğrulaması gerektiriyor.
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").
                        allowedHeaders("*")
                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
            }
        };
    }
/*
    private static final String [] AUTH_WHITE_LIST= {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "swagger-ui/**",
            "/",
            "index.html",
            "/images/**",
            "/css/**",
            "/js/**"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring().requestMatchers(AUTH_WHITE_LIST);
            }
        };
    }
*/

 /*   @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/swagger-ui.html", "/v3/api-docs/**").permitAll() // Swagger UI'ye erişimi izin verir
                .anyRequest().authenticated() // Diğer tüm isteklere kimlik doğrulama gerektirir
                .and()
                .httpBasic(Customizer.withDefaults()); // Temel HTTP kimlik doğrulama (gerekiyorsa)
    }*/
}
