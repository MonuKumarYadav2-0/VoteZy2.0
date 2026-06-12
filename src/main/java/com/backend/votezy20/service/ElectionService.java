package com.backend.votezy20.service;

import java.util.List;

import com.backend.votezy20.requestDTO.ElectionCreateRequest;
import com.backend.votezy20.requestDTO.ElectionStatusRequest;
import com.backend.votezy20.responseDTO.ElectionResponse;

public interface ElectionService {

    ElectionResponse create(
            String orgCode,
            ElectionCreateRequest request
    );

    List<ElectionResponse> getAll(
            String orgCode
    );

    List<ElectionResponse> getByStatus(
            String orgCode,
            boolean active
    );

    ElectionResponse getByCode(
            String orgCode,
            String electionCode
    );

    void toggleStatus(
            String orgCode,
            ElectionStatusRequest request
    );

    void delete(
            String orgCode,
            String electionCode
    );
}