package com.backend.votezy20.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.votezy20.requestDTO.EnrollCandidateRequest;
import com.backend.votezy20.responseDTO.ApiResponse;
import com.backend.votezy20.responseDTO.CandidateResponse;
import com.backend.votezy20.service.CandidateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
@Validated
public class CandidateController {
	@Autowired
	private  CandidateService candidateService;

	// ORG ONLY
	@PostMapping("/enroll")
	public ResponseEntity<ApiResponse<CandidateResponse>> enroll(Authentication authentication,

			@Valid @RequestBody EnrollCandidateRequest request) {

		String orgCode = authentication.getName();

		CandidateResponse response = candidateService.enroll(orgCode, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Candidate enrolled successfully", response));
	}

	// ORG + VOTER
	@GetMapping("/election/{electionCode}")
	public ResponseEntity<ApiResponse<List<CandidateResponse>>> getByElection(@PathVariable String electionCode) {

		List<CandidateResponse> response = candidateService.getByElection(electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Candidates fetched successfully", response));
	}

	// ORG + VOTER
	@GetMapping("/{candidateCode}")
	public ResponseEntity<ApiResponse<CandidateResponse>> getByCode(@PathVariable String candidateCode) {

		CandidateResponse response = candidateService.getByCode(candidateCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Candidate fetched successfully", response));
	}

//	// ORG ONLY
//	@DeleteMapping("/remove/" + "{electionCode}/" + "{candidateCode}")
//	public ResponseEntity<ApiResponse<String>> removeFromElection(Authentication authentication,
//
//			@PathVariable String electionCode,
//
//			@PathVariable String candidateCode) {
//
//		String orgCode = authentication.getName();
//
//		candidateService.
//
//		return ResponseEntity.ok(new ApiResponse<>(true, "Candidate removed from election successfully", null));
//	}
}