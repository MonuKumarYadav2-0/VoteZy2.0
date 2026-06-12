package com.backend.votezy20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.votezy20.requestDTO.ChangePasswordRequest;
import com.backend.votezy20.requestDTO.OrgLoginRequest;
import com.backend.votezy20.requestDTO.OrgRegisterRequest;
import com.backend.votezy20.requestDTO.UpdateOrgProfileRequest;
import com.backend.votezy20.requestDTO.VerifyOtpRequest;
import com.backend.votezy20.responseDTO.ApiResponse;
import com.backend.votezy20.responseDTO.AuthResponse;
import com.backend.votezy20.responseDTO.OrgProfileResponse;
import com.backend.votezy20.service.OrgService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/org")
@RequiredArgsConstructor
@Validated
public class OrgController {

	@Autowired
	private  OrgService orgService;

	// PUBLIC
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody OrgRegisterRequest request) {

		orgService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Organization registered successfully. Verify OTP.", null));
	}

	// PUBLIC
	@PostMapping("/verify_otp")
	public ResponseEntity<ApiResponse<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {

		orgService.verifyOtp(request);

		return ResponseEntity.ok(new ApiResponse<>(true, "OTP verified successfully", null));
	}

	// PUBLIC
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody OrgLoginRequest request) {

		String token = orgService.login(request);

		AuthResponse response = AuthResponse.builder().token(token).role("ROLE_ORG").build();

		return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
	}

	// PRIVATE (ROLE_ORG)
	@GetMapping("/profile")
	public ResponseEntity<ApiResponse<OrgProfileResponse>> getProfile(Authentication authentication) {

		String orgCode = authentication.getName();

		OrgProfileResponse response = orgService.getProfile(orgCode);

		return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", response));
	}

	// PRIVATE
	@PutMapping("/update_profile")
	public ResponseEntity<ApiResponse<String>> updateProfile(Authentication authentication,

			@Valid @RequestBody UpdateOrgProfileRequest request) {

		String orgCode = authentication.getName();

		orgService.updateProfile(orgCode, request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", null));
	}

	// PRIVATE
	@PutMapping("/change_password")
	public ResponseEntity<ApiResponse<String>> changePassword(Authentication authentication,

			@Valid @RequestBody ChangePasswordRequest request) {

		String orgCode = authentication.getName();

		orgService.changePassword(orgCode, request);

		return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully", null));
	}
}