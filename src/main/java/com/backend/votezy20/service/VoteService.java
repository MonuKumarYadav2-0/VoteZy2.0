package com.backend.votezy20.service;

import java.util.List;

import com.backend.votezy20.requestDTO.CastVoteRequest;
import com.backend.votezy20.responseDTO.VoteResponse;

public interface VoteService {

    void castVote(
            String voterCode,
            CastVoteRequest request
    );

    boolean hasVoted(
            String voterCode,
            String electionCode
    );

    List<VoteResponse>
    getByElection(
            String orgCode,
            String electionCode
    );

	VoteResponse getMyVote(String voterCode, String electionCode);
}