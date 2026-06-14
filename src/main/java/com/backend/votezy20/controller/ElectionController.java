package com.backend.votezy20.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
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

	private final ElectionService electionService;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<ElectionResponse>> create(Authentication authentication,
			@Valid @RequestBody CreateElectionRequest request) {

		String orgCode = authentication.getName();
		ElectionResponse response = electionService.create(orgCode, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Election created successfully", response));
	}

	@PutMapping("/toggle_status/{electionCode}")
	public ResponseEntity<ApiResponse<ElectionResponse>> toggleStatus(Authentication authentication,
			@PathVariable String electionCode) {

		String orgCode = authentication.getName();
		ElectionResponse response = electionService.deactivate(orgCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Election status updated", response));
	}

	@GetMapping("/all")
	public ResponseEntity<ApiResponse<List<ElectionResponse>>> getAll(Authentication authentication) {
		String orgCode = authentication.getName();
		List<ElectionResponse> response = electionService.getAll(orgCode);
		return ResponseEntity.ok(new ApiResponse<>(true, "Elections fetched successfully", response));
	}

	@GetMapping("/{electionCode}")
	public ResponseEntity<ApiResponse<ElectionResponse>> getByCode(Authentication authentication,
			@PathVariable String electionCode) {

		String orgCode = authentication.getName();
		ElectionResponse response = electionService.getByCode(orgCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Election fetched successfully", response));
	}

	@DeleteMapping("/{electionCode}")
	public ResponseEntity<ApiResponse<String>> delete(Authentication authentication,
			@PathVariable String electionCode) {

		String orgCode = authentication.getName();
		electionService.delete(orgCode, electionCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Election deleted successfully", null));
	}

	@GetMapping("/status")
	public ResponseEntity<ApiResponse<List<ElectionResponse>>> getByStatus(Authentication authentication,
			@RequestParam("active") boolean active) {

		String orgCode = authentication.getName();
		List<ElectionResponse> response = electionService.getByStatus(orgCode, active);
		return ResponseEntity.ok(new ApiResponse<>(true, "Elections fetched successfully", response));
	}
	
	@GetMapping("/voter")
	public ResponseEntity<ApiResponse<List<ElectionResponse>>> getForVoter(Authentication authentication) {

	    String voterCode = authentication.getName();

	    List<ElectionResponse> response = electionService.getForVoter(voterCode);

	    return ResponseEntity.ok(
	            new ApiResponse<>(true, "Voter elections fetched successfully", response)
	    );
	}

	@GetMapping("/voter/{electionCode}")
	public ResponseEntity<ApiResponse<ElectionResponse>> getForVoterByCode(
	        Authentication authentication,
	        @PathVariable String electionCode) {

	    String voterCode = authentication.getName();

	    ElectionResponse response = electionService.getForVoterByCode(voterCode, electionCode);

	    return ResponseEntity.ok(
	            new ApiResponse<>(true, "Election fetched successfully", response)
	    );
	}
}