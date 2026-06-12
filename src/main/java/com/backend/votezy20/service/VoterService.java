package com.backend.votezy20.service;

import org.springframework.web.multipart.MultipartFile;

import com.backend.votezy20.requestDTO.AddSingleVoterRequest;
import com.backend.votezy20.requestDTO.SetPasswordRequest;
import com.backend.votezy20.requestDTO.VoterLoginRequest;
import com.backend.votezy20.responseDTO.PagedResponse;
import com.backend.votezy20.responseDTO.VoterProfileResponse;

public interface VoterService {

    void addSingleVoter(
            String orgCode,
            AddSingleVoterRequest request
    );

    void uploadVotersCsv(
            String orgCode,
            MultipartFile file
    );

    void setPassword(
            SetPasswordRequest request
    );

    String login(
            VoterLoginRequest request
    );

    VoterProfileResponse getProfile(
            String voterCode
    );

    PagedResponse<VoterProfileResponse>
    getAllVoters(
            String orgCode,
            int page,
            int size
    );

    void deactivateVoter(
            String orgCode,
            String voterCode
    );

    void activateVoter(
            String orgCode,
            String voterCode
    );
}