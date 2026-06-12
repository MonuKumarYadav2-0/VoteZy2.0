package com.backend.votezy20.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.OtpToken;

@Repository
public interface OtpTokenRepository
        extends JpaRepository<OtpToken, Long> {

    // Verify OTP
    Optional<OtpToken>
    findTopByEmailAndOtpAndUsedFalseOrderByCreatedAtDesc(
            String email,
            String otp
    );

    // Latest OTP
    Optional<OtpToken>
    findTopByEmailOrderByCreatedAtDesc(
            String email
    );

    // Cleanup expired OTPs
    void deleteByExpiresAtBefore(
            LocalDateTime now
    );
}