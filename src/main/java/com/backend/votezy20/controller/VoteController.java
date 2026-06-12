package com.backend.votezy20.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.votezy20.requestDTO.CastVoteRequest;
import com.backend.votezy20.responseDTO.ApiResponse;
import com.backend.votezy20.responseDTO.VoteResponse;
import com.backend.votezy20.service.VoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@Validated
public class VoteController {
	@Autowired
	private  VoteService voteService;

	// VOTER ONLY
	@PostMapping("/cast")
	public ResponseEntity<ApiResponse<Void>> castVote(Authentication authentication,

			@Valid @RequestBody CastVoteRequest request) {

		String voterCode = authentication.getName();

	    voteService.castVote(voterCode, request);

		return new ResponseEntity<ApiResponse<Void>>(null);
	}

	// ORG ONLY
	@GetMapping("/election/{electionCode}")
	public ResponseEntity<ApiResponse<List<VoteResponse>>> getVotesByElection(Authentication authentication,

			@PathVariable String electionCode) {

		String orgCode = authentication.getName();

		List<VoteResponse> response = voteService.getByElection(orgCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Votes fetched successfully", response));
	}

	// VOTER ONLY
	@GetMapping("/my_vote/{electionCode}")
	public ResponseEntity<ApiResponse<VoteResponse>> getMyVote(Authentication authentication,

			@PathVariable String electionCode) {

		String voterCode = authentication.getName();

		VoteResponse response = voteService.getMyVote(voterCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Vote fetched successfully", response));
	}
}