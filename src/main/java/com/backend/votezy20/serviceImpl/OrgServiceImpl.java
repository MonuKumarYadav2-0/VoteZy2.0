package com.backend.votezy20.serviceImpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.Organization;
import com.backend.votezy20.entitiy.OtpToken;
import com.backend.votezy20.repositories.OrgRepository;
import com.backend.votezy20.repositories.VoterRepository;
import com.backend.votezy20.requestDTO.ChangePasswordRequest;
import com.backend.votezy20.requestDTO.OrgLoginRequest;
import com.backend.votezy20.requestDTO.OrgRegisterRequest;
import com.backend.votezy20.requestDTO.UpdateOrgProfileRequest;
import com.backend.votezy20.requestDTO.VerifyOtpRequest;
import com.backend.votezy20.responseDTO.OrgProfileResponse;
import com.backend.votezy20.security.JwtUtil;
import com.backend.votezy20.service.EmailService;
import com.backend.votezy20.service.OrgService;
import com.backend.votezy20.service.OtpService;
import com.backend.votezy20.util.CodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrgServiceImpl implements OrgService {

	private final OrgRepository orgRepository;
	private final VoterRepository voterRepository;
	private final OtpService otpService;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public void register(OrgRegisterRequest request) {

		if (orgRepository.existsByEmail(request.getEmail())) {

			throw new RuntimeException("Organization email already exists");
		}

		Organization organization = Organization.builder().orgCode(CodeGenerator.generateOrgCode())
				.orgName(request.getOrgName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).address(request.getAddress()).isVerified(false)
				.isActive(true).build();

		orgRepository.save(organization);

		OtpToken otpToken = otpService.createOtp(request.getEmail());

		emailService.sendOtpEmail(request.getEmail(), otpToken.getOtp());
	}

	@Override
	public void verifyOtp(VerifyOtpRequest request) {

		Organization organization = orgRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		boolean verified = otpService.verifyOtp(request.getEmail(), request.getOtp());

		if (!verified) {

			throw new RuntimeException("Invalid or expired OTP");
		}

		organization.setIsVerified(true);

		orgRepository.save(organization);
	}

	@Override
	public String login(OrgLoginRequest request) {

		Organization organization = orgRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), organization.getPassword())) {

			throw new RuntimeException("Invalid credentials");
		}

		if (!organization.getIsVerified()) {

			throw new RuntimeException("Please verify your email");
		}

		if (!organization.getIsActive()) {

			throw new RuntimeException("Organization account inactive");
		}

		return jwtUtil.generateToken(organization.getOrgCode(), "ROLE_ORG");
	}

	@Override
	@Cacheable(value = "organization", key = "#orgCode")
	@Transactional(readOnly = true)
	public OrgProfileResponse getProfile(String orgCode) {

		Organization organization = orgRepository.findByOrgCode(orgCode)
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		return OrgProfileResponse.builder().orgCode(organization.getOrgCode()).orgName(organization.getOrgName())
				.email(organization.getEmail()).address(organization.getAddress())
				.isVerified(organization.getIsVerified()).createdAt(organization.getCreatedAt()).build();
	}

	@Override
	@CacheEvict(value = "organization", key = "#orgCode")
	public void updateProfile(String orgCode, UpdateOrgProfileRequest request) {

		Organization organization = orgRepository.findByOrgCode(orgCode)
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		organization.setOrgName(request.getOrgName());

		organization.setAddress(request.getAddress());

		orgRepository.save(organization);
	}

	@Override
	public void changePassword(String orgCode, ChangePasswordRequest request) {

		Organization organization = orgRepository.findByOrgCode(orgCode)
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		if (!passwordEncoder.matches(request.getOldPassword(), organization.getPassword())) {

			throw new RuntimeException("Old password incorrect");
		}

		organization.setPassword(passwordEncoder.encode(request.getNewPassword()));

		orgRepository.save(organization);
	}

	@Override
	public void resendInvite(String orgCode, String email) {

		orgRepository.findByOrgCode(orgCode).orElseThrow(() -> new RuntimeException("Organization not found"));

		var voter = voterRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Voter not found"));

		String token = voter.getVoterCode();

		emailService.sendVoterInviteEmail(voter.getEmail(), voter.getName(), token);
	}
}