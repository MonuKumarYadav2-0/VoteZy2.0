package com.backend.votezy20.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.backend.votezy20.entitiy.Organization;
import com.backend.votezy20.entitiy.Voter;
import com.backend.votezy20.repositories.OrgRepository;
import com.backend.votezy20.repositories.VoterRepository;
import com.backend.votezy20.requestDTO.AddSingleVoterRequest;
import com.backend.votezy20.requestDTO.SetPasswordRequest;
import com.backend.votezy20.requestDTO.VoterLoginRequest;
import com.backend.votezy20.responseDTO.PagedResponse;
import com.backend.votezy20.responseDTO.VoterProfileResponse;
import com.backend.votezy20.security.JwtUtil;
import com.backend.votezy20.service.EmailService;
import com.backend.votezy20.service.VoterService;
import com.backend.votezy20.util.CodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VoterServiceImpl implements VoterService {

	private final VoterRepository voterRepository;
	private final OrgRepository orgRepository;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	@CacheEvict(value = "voters", allEntries = true)
	public void addSingleVoter(String orgCode, AddSingleVoterRequest request) {

		Organization organization = orgRepository.findByOrgCode(orgCode)
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		if (voterRepository.existsByEmail(request.getEmail())) {

			throw new RuntimeException("Voter email already exists");
		}

		Voter voter = Voter.builder().voterCode(CodeGenerator.generateVoterCode()).name(request.getName())
				.email(request.getEmail()).organization(organization).isActive(true).isPasswordSet(false).build();

		voterRepository.save(voter);

		String token = voter.getVoterCode();

		emailService.sendVoterInviteEmail(voter.getEmail(), voter.getName(), token);
	}

	@Override
	@CacheEvict(value = "voters", allEntries = true)
	public void uploadVotersCsv(String orgCode, MultipartFile file) {

		try {

			Organization organization = orgRepository.findByOrgCode(orgCode)
					.orElseThrow(() -> new RuntimeException("Organization not found"));

			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

			String line;

			while ((line = reader.readLine()) != null) {

				String[] data = line.split(",");

				String name = data[0].trim();

				String email = data[1].trim();

				if (voterRepository.existsByEmail(email)) {
					continue;
				}

				Voter voter = Voter.builder().voterCode(CodeGenerator.generateVoterCode()).name(name).email(email)
						.organization(organization).isActive(true).isPasswordSet(false).build();

				voterRepository.save(voter);

				emailService.sendVoterInviteEmail(voter.getEmail(), voter.getName(), voter.getVoterCode());
			}

		} catch (Exception e) {

			throw new RuntimeException("CSV upload failed");
		}
	}

	@Override
	public void setPassword(SetPasswordRequest request) {

		Voter voter = voterRepository.findByVoterCode(request.getToken())
				.orElseThrow(() -> new RuntimeException("Invalid token"));

		voter.setPassword(passwordEncoder.encode(request.getPassword()));

		voter.setIsPasswordSet(true);

		voterRepository.save(voter);

		emailService.sendPasswordSetupConfirmation(voter.getEmail(), voter.getName());
	}

	@Override
	public String login(VoterLoginRequest request) {

		Voter voter = voterRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!voter.getIsPasswordSet()) {

			throw new RuntimeException("Please setup password first");
		}

		if (!voter.getIsActive()) {

			throw new RuntimeException("Voter account inactive");
		}

		if (!passwordEncoder.matches(request.getPassword(), voter.getPassword())) {

			throw new RuntimeException("Invalid credentials");
		}

		return jwtUtil.generateToken(voter.getVoterCode(), "ROLE_VOTER");
	}

	@Override
	@Transactional(readOnly = true)
	public VoterProfileResponse getProfile(String voterCode) {

		Voter voter = voterRepository.findByVoterCode(voterCode)
				.orElseThrow(() -> new RuntimeException("Voter not found"));

		return VoterProfileResponse.builder().voterCode(voter.getVoterCode()).name(voter.getName())
				.email(voter.getEmail()).isActive(voter.getIsActive()).isPasswordSet(voter.getIsPasswordSet())
				.createdAt(voter.getCreatedAt()).orgCode(voter.getOrganization().getOrgCode()).build();
	}

	@Override
	@Cacheable(value = "voters", key = "#orgCode + ':' + #page + ':' + #size")
	@Transactional(readOnly = true)
	public PagedResponse<VoterProfileResponse> getAllVoters(String orgCode, int page, int size) {

		Page<Voter> voterPage = voterRepository.findByOrganization_OrgCode(orgCode, PageRequest.of(page, size));

		List<VoterProfileResponse> responses = voterPage.getContent().stream()
				.map(voter -> VoterProfileResponse.builder().voterCode(voter.getVoterCode()).name(voter.getName())
						.email(voter.getEmail()).isActive(voter.getIsActive()).isPasswordSet(voter.getIsPasswordSet())
						.createdAt(voter.getCreatedAt()).orgCode(voter.getOrganization().getOrgCode()).build())
				.toList();

		return PagedResponse.<VoterProfileResponse>builder().content(responses).pageNumber(page).pageSize(size)
				.totalElements(voterPage.getTotalElements()).totalPages(voterPage.getTotalPages()).build();
	}

	@Override
	@CacheEvict(value = "voters", allEntries = true)
	public void deactivateVoter(String orgCode, String voterCode) {

		Voter voter = voterRepository.findByVoterCodeAndOrganization_OrgCode(voterCode, orgCode)
				.orElseThrow(() -> new RuntimeException("Voter not found"));

		voter.setIsActive(false);

		voterRepository.save(voter);
	}

	@Override
	@CacheEvict(value = "voters", allEntries = true)
	public void activateVoter(String orgCode, String voterCode) {

		Voter voter = voterRepository.findByVoterCodeAndOrganization_OrgCode(voterCode, orgCode)
				.orElseThrow(() -> new RuntimeException("Voter not found"));

		voter.setIsActive(true);

		voterRepository.save(voter);
	}
}