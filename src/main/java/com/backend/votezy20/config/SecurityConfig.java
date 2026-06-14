package com.backend.votezy20.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.backend.votezy20.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtFilter;

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

        .csrf(
                csrf ->
                        csrf.disable()
        )

        .cors(
                Customizer.withDefaults()
        )

        .sessionManagement(
                session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
        )

        .authorizeHttpRequests(
                auth -> auth

                // ==================
                // PUBLIC APIs
                // ==================
                .requestMatchers(

                        // ORG AUTH
                        "/api/org/register",
                        "/api/org/login",
                        "/api/org/verify_otp",

                        // VOTER AUTH
                        "/api/voter/login",
                        "/api/voter/set_password",

                        // RESULTS
                        "/api/result/**",

                        // WEBSOCKET
                        "/ws/**"

                ).permitAll()

                // ==================
                // TEST MODE
                // Just token required
                // ==================

                // election APIs
                .requestMatchers(
                        "/api/election/**"
                ).authenticated()

                // candidate APIs
                .requestMatchers(
                        "/api/candidate/**"
                ).authenticated()

                // vote APIs
                .requestMatchers(
                        "/api/vote/**"
                ).authenticated()

                // voter APIs
                .requestMatchers(
                        "/api/voter/**"
                ).authenticated()

                // org APIs
                .requestMatchers(
                        "/api/org/**"
                ).authenticated()

                // everything else
                .anyRequest()
                .authenticated()
        )

        .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder
    passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager
    authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config
                .getAuthenticationManager();
    }
}