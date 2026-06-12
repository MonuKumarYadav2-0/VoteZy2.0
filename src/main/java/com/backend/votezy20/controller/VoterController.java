package com.backend.votezy20.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.backend.votezy20.requestDTO.SetPasswordRequest;
import com.backend.votezy20.requestDTO.VoterLoginRequest;
import com.backend.votezy20.responseDTO.ApiResponse;
import com.backend.votezy20.responseDTO.AuthResponse;
import com.backend.votezy20.responseDTO.PagedResponse;
import com.backend.votezy20.responseDTO.VoterProfileResponse;
import com.backend.votezy20.service.VoterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/voter")
@RequiredArgsConstructor
@Validated
public class VoterController {
	@Autowired
	private VoterService voterService;

	// ORG ONLY - CSV Upload
	@PostMapping("/upload_csv")
	public ResponseEntity<ApiResponse<String>> uploadCsv(Authentication authentication,

			@RequestParam("file") MultipartFile file) {

		String orgCode = authentication.getName();

		voterService.uploadVotersCsv(orgCode, file);

		return ResponseEntity.ok(new ApiResponse<>(true, "CSV uploaded successfully", null));
	}

	// PUBLIC
	@PostMapping("/set_password")
	public ResponseEntity<ApiResponse<String>> setPassword(@Valid @RequestBody SetPasswordRequest request) {

		voterService.setPassword(request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Password set successfully", null));
	}

	// PUBLIC
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody VoterLoginRequest request) {

		String token = voterService.login(request);

		AuthResponse response = AuthResponse.builder().token(token).role("ROLE_VOTER").build();

		return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
	}

	// ORG ONLY
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<PagedResponse<VoterProfileResponse>>> getAllVoters(

			Authentication authentication,

			@RequestParam(defaultValue = "0") int page,

			@RequestParam(defaultValue = "5") int size) {

		String orgCode = authentication.getName();

		PagedResponse<VoterProfileResponse> response = voterService.getAllVoters(orgCode, page, size);

		return ResponseEntity.ok(new ApiResponse<>(true, "Voters fetched successfully", response));
	}

	// ORG ONLY
	@GetMapping("/{voterCode}")
	public ResponseEntity<ApiResponse<VoterProfileResponse>> getByCode(Authentication authentication,

			@PathVariable String voterCode) {

		String orgCode = authentication.getName();

		VoterProfileResponse response = voterService.getByCode(orgCode, voterCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Voter fetched successfully", response));
	}

	// ORG ONLY
	@PutMapping("/toggle_status/{voterCode}")
	public ResponseEntity<ApiResponse<String>> toggleStatus(Authentication authentication,

			@PathVariable String voterCode) {

		String orgCode = authentication.getName();

		voterService.toggleVoterStatus(orgCode, voterCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Voter status updated", null));
	}

	// ORG ONLY
	@DeleteMapping("/{voterCode}")
	public ResponseEntity<ApiResponse<String>> delete(Authentication authentication,

			@PathVariable String voterCode) {

		String orgCode = authentication.getName();

		voterService.deactivateVoter(orgCode, voterCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Voter deleted successfully", null));
	}
}