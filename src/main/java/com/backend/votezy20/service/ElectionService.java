package com.backend.votezy20.service;

import java.util.List;

import com.backend.votezy20.requestDTO.CreateElectionRequest;
import com.backend.votezy20.requestDTO.ElectionCreateRequest;
import com.backend.votezy20.requestDTO.ElectionStatusRequest;
import com.backend.votezy20.responseDTO.ElectionResponse;

import jakarta.validation.Valid;

public interface ElectionService {

	ElectionResponse create(String orgCode, @Valid CreateElectionRequest request);

	List<ElectionResponse> getAll(String orgCode);

	List<ElectionResponse> getByStatus(String orgCode, boolean active);

	ElectionResponse getByCode( String electionCode);

	void toggleStatus(String orgCode, String electionCode);

	void delete(String orgCode, String electionCode);

	ElectionResponse create(String orgCode, ElectionCreateRequest request);

	void toggleStatus(String orgCode, ElectionStatusRequest request);

	ElectionResponse getByCode(String orgCode, String electionCode);
}