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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.votezy20.requestDTO.CreateElectionRequest;
import com.backend.votezy20.responseDTO.ApiResponse;
import com.backend.votezy20.responseDTO.ElectionResponse;
import com.backend.votezy20.service.ElectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/election")
@RequiredArgsConstructor
@Validated
public class ElectionController {

	@Autowired
	private ElectionService electionService;

	// ORG ONLY
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<ElectionResponse>> create(Authentication authentication,

			@Valid @RequestBody CreateElectionRequest request) {

		String orgCode = authentication.getName();

		ElectionResponse response = electionService.create(orgCode, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Election created successfully", response));
	}

//	// ORG ONLY
//	@PutMapping("/update/{electionCode}")
//	public ResponseEntity<ApiResponse<ElectionResponse>> update(Authentication authentication,
//
//			@PathVariable String electionCode,
//
//			@Valid @RequestBody UpdateElectionRequest request) {
//
//		String orgCode = authentication.getName();
//
//		ElectionResponse response = electionService.update(orgCode, electionCode, request);
//
//		return ResponseEntity.ok(new ApiResponse<>(true, "Election updated successfully", response));
//	}

	// ORG ONLY
	@PutMapping("/toggle_status/{electionCode}")
	public ResponseEntity<ApiResponse<String>> toggleStatus(Authentication authentication,

			@PathVariable String electionCode) {

		String orgCode = authentication.getName();

		electionService.toggleStatus(orgCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Election status updated", null));
	}

	// ORG ONLY
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<ElectionResponse>>> getAll(Authentication authentication) {

		String orgCode = authentication.getName();

		List<ElectionResponse> response = electionService.getAll(orgCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Elections fetched successfully", response));
	}

	// ORG + VOTER
	@GetMapping("/{electionCode}")
	public ResponseEntity<ApiResponse<ElectionResponse>> getByCode(@PathVariable String electionCode) {

		ElectionResponse response = electionService.getByCode(electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Election fetched successfully", response));
	}

	// ORG ONLY
	@DeleteMapping("/{electionCode}")
	public ResponseEntity<ApiResponse<String>> delete(Authentication authentication,

			@PathVariable String electionCode) {

		String orgCode = authentication.getName();

		electionService.delete(orgCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Election deleted successfully", null));
	}
}