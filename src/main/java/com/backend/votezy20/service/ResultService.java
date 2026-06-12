package com.backend.votezy20.service;

import java.util.List;

import com.backend.votezy20.responseDTO.ElectionResultResponse;

public interface ResultService {

    ElectionResultResponse
    announce(
            String orgCode,
            String electionCode
    );

    ElectionResultResponse
    getResult(
            String electionCode
    );

    List<ElectionResultResponse>
    getAllResults(
            String orgCode
    );

    void deleteResult(
            String orgCode,
            String electionCode
    );
}