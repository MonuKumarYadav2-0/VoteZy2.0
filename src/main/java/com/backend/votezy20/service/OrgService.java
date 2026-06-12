package com.backend.votezy20.service;

import com.backend.votezy20.requestDTO.ChangePasswordRequest;
import com.backend.votezy20.requestDTO.OrgLoginRequest;
import com.backend.votezy20.requestDTO.OrgRegisterRequest;
import com.backend.votezy20.requestDTO.UpdateOrgProfileRequest;
import com.backend.votezy20.requestDTO.VerifyOtpRequest;
import com.backend.votezy20.responseDTO.OrgProfileResponse;

public interface OrgService {

    void register(
            OrgRegisterRequest request
    );

    void verifyOtp(
            VerifyOtpRequest request
    );

    String login(
            OrgLoginRequest request
    );

    OrgProfileResponse getProfile(
            String orgCode
    );

    void updateProfile(
            String orgCode,
            UpdateOrgProfileRequest request
    );

    void changePassword(
            String orgCode,
            ChangePasswordRequest request
    );

    void resendInvite(
            String orgCode,
            String email
    );
}