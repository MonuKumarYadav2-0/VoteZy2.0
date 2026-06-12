package com.backend.votezy20.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.backend.votezy20.responseDTO.ElectionResultResponse;
import com.backend.votezy20.service.ResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController {

	private final ResultService resultService;

	// Announce Result
	@PostMapping("/announce/{electionCode}")
	public ResponseEntity<ElectionResultResponse> announceResult(@PathVariable String electionCode,
			Authentication authentication) {

		String orgCode = authentication.getName();

		ElectionResultResponse response = resultService.announce(orgCode, electionCode);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Get Single Result
	@GetMapping("/{electionCode}")
	public ResponseEntity<ElectionResultResponse> getResult(@PathVariable String electionCode) {

		return ResponseEntity.ok(resultService.getResult(electionCode));
	}

	// Get All Results of Organization
	@GetMapping("/all")
	public ResponseEntity<List<ElectionResultResponse>> getAllResults(Authentication authentication) {

		String orgCode = authentication.getName();

		return ResponseEntity.ok(resultService.getAllResults(orgCode));
	}

	// Delete Result
	@DeleteMapping("/{electionCode}")
	public ResponseEntity<String> deleteResult(@PathVariable String electionCode, Authentication authentication) {

		String orgCode = authentication.getName();

		resultService.deleteResult(orgCode, electionCode);

		return ResponseEntity.ok("Result deleted successfully");
	}
}