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

	private final JwtAuthenticationFilter jwtFilter;
	//private final PasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.cors(Customizer.withDefaults())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						// PUBLIC ORG
						.requestMatchers("/api/org/register", "/api/org/login", "/api/org/verify_otp").permitAll()

						// PUBLIC VOTER
						.requestMatchers("/api/voter/login", "/api/voter/set_password").permitAll()

						// PUBLIC RESULT
						.requestMatchers("/api/result/*").permitAll()

						// WEBSOCKET
						.requestMatchers("/ws/**").permitAll()

						// ORG ONLY
						.requestMatchers("/api/org/**", "/api/election/**", "/api/candidate/**").hasRole("ORG")

						// VOTER ONLY
						.requestMatchers("/api/vote/cast", "/api/vote/my_vote/**").hasRole("VOTER")

						// ORG VOTE ACCESS
						.requestMatchers("/api/vote/election/**").hasRole("ORG")

						.anyRequest().authenticated())

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}
}