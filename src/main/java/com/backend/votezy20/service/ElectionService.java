package com.backend.votezy20.service;

import java.util.List;

import com.backend.votezy20.requestDTO.CreateElectionRequest;
import com.backend.votezy20.responseDTO.ElectionResponse;

import jakarta.validation.Valid;

public interface ElectionService {

	ElectionResponse create(String orgCode, @Valid CreateElectionRequest request);

	List<ElectionResponse> getAll(String orgCode);


	void delete(String orgCode, String electionCode);

	ElectionResponse getByCode(String orgCode, String electionCode);

	ElectionResponse deactivate(String orgCode, String electionCode);

	List<ElectionResponse> getByStatus(String orgCode, boolean active);

	ElectionResponse getForVoterByCode(String voterCode, String electionCode);

	List<ElectionResponse> getForVoter(String voterCode);

}