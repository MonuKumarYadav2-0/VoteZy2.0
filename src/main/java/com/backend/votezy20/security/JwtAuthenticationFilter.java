
package com.backend.votezy20.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException,
            IOException {

        String token =
                getJwtFromRequest(
                        request
                );

        if (token != null
                && jwtUtil
                        .isTokenValid(
                                token
                        )) {

            String code =
                    jwtUtil.extractCode(
                            token
                    );

            String role =
                    jwtUtil.extractRole(
                            token
                    );

            UsernamePasswordAuthenticationToken
                    authentication =
                    new UsernamePasswordAuthenticationToken(
                            code,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            role
                                    )
                            )
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(
                                    request
                            )
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            authentication
                    );
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    private String getJwtFromRequest(
            HttpServletRequest request
    ) {

        String bearerToken =
                request.getHeader(
                        "Authorization"
                );

        if (bearerToken != null
                && bearerToken.startsWith(
                        "Bearer "
                )) {

            return bearerToken.substring(
                    7
            );
        }

        return null;
    }
}

