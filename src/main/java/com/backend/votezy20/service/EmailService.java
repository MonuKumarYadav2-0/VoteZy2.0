package com.backend.votezy20.service;

public interface EmailService {

    void sendOtpEmail(
            String to,
            String otp
    );

    void sendVoterInviteEmail(
            String to,
            String name,
            String token
    );

    void sendPasswordSetupConfirmation(
            String to,
            String name
    );

    void sendElectionNotification(
            String to,
            String name,
            String electionName
    );
}