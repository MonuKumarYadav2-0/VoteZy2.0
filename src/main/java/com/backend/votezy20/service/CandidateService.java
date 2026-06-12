package com.backend.votezy20.service;

import java.util.List;

import com.backend.votezy20.requestDTO.EnrollCandidateRequest;
import com.backend.votezy20.responseDTO.CandidateResponse;

public interface CandidateService {

    CandidateResponse enroll(
            String orgCode,
            EnrollCandidateRequest request
    );

    List<CandidateResponse>
    getByElection(
            String electionCode
    );

    CandidateResponse getByCode(
            String candidateCode
    );
}