package com.backend.votezy20.service;

import com.backend.votezy20.entitiy.OtpToken;

public interface OtpService {

    String generateOtp();

    OtpToken createOtp(
            String email
    );

    boolean verifyOtp(
            String email,
            String otp
    );

    void invalidateOtp(
            OtpToken otpToken
    );
}