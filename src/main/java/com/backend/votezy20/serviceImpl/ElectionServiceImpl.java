package com.backend.votezy20.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.entitiy.Organization;
import com.backend.votezy20.entitiy.Voter;
import com.backend.votezy20.repositories.ElectionRepository;
import com.backend.votezy20.repositories.OrgRepository;
import com.backend.votezy20.repositories.VoteRepository;
import com.backend.votezy20.repositories.VoterRepository;
import com.backend.votezy20.requestDTO.CreateElectionRequest;
import com.backend.votezy20.responseDTO.ElectionResponse;
import com.backend.votezy20.service.ElectionService;
import com.backend.votezy20.util.CodeGenerator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ElectionServiceImpl implements ElectionService {

	private final ElectionRepository electionRepository;

	private final OrgRepository orgRepository;

	private final VoterRepository voterRepository;

	private final VoteRepository voteRepository;

	@Override
	@CacheEvict(value = { "elections", "election" }, allEntries = true)
	public ElectionResponse create(String orgCode, @Valid CreateElectionRequest request) {

		Organization organization = orgRepository.findByOrgCode(orgCode)
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		if (request.getStartTime().isAfter(request.getEndTime())) {

			throw new RuntimeException("Start time cannot be after end time");
		}

		Election election = Election.builder().electionCode(CodeGenerator.generateElectionCode())
				.name(request.getName()).startTime(request.getStartTime()).endTime(request.getEndTime()).isActive(true)
				.organization(organization).build();

		electionRepository.save(election);

		return mapToResponse(election);
	}

	@Override
	@Cacheable(value = "elections", key = "#orgCode")
	@Transactional(readOnly = true)
	public List<ElectionResponse> getAll(String orgCode) {

		return electionRepository.findByOrganization_OrgCode(orgCode).stream().map(this::mapToResponse).toList();
	}

	@Override
	@Cacheable(value = "elections", key = "#orgCode + '_' + #active")
	@Transactional(readOnly = true)
	public List<ElectionResponse> getByStatus(String orgCode, boolean active) {

		return electionRepository.findByOrganization_OrgCodeAndIsActive(orgCode, active).stream()
				.map(this::mapToResponse).toList();
	}

	@Override
	@Cacheable(value = "election", key = "#electionCode")
	@Transactional(readOnly = true)
	public ElectionResponse getByCode(String orgCode, String electionCode) {

		Election election = electionRepository.findByElectionCodeAndOrganization_OrgCode(electionCode, orgCode)
				.orElseThrow(() -> new RuntimeException("Election not found"));

		return mapToResponse(election);
	}

	@Override
	@CacheEvict(value = { "elections", "election" }, allEntries = true)
	public ElectionResponse deactivate(String orgCode, String electionCode) {

		Election election = electionRepository.findByElectionCodeAndOrganization_OrgCode(electionCode, orgCode)
				.orElseThrow(() -> new RuntimeException("Election not found"));

		election.setIsActive(false);

		Election saved = electionRepository.save(election);

		return mapToResponse(saved);
	}

	@Override
	@CacheEvict(value = { "elections", "election", "candidates" }, allEntries = true)
	public void delete(String orgCode, String electionCode) {

		Election election = electionRepository.findByElectionCodeAndOrganization_OrgCode(electionCode, orgCode)
				.orElseThrow(() -> new RuntimeException("Election not found"));

		electionRepository.delete(election);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElectionResponse> getForVoter(String voterCode) {
		 System.out.println(
		            "JWT voterCode = "
		                    + voterCode
		    );

		Voter voter = voterRepository.findByVoterCode(voterCode)
				.orElseThrow(() -> new RuntimeException("Voter not found"));

		String orgCode = voter.getOrganization().getOrgCode();

		LocalDateTime now = LocalDateTime.now();

		return electionRepository.findByOrganization_OrgCodeAndIsActive(orgCode, true).stream()

				.filter(election -> !voteRepository.existsByVoter_VoterCodeAndElection_ElectionCode(voterCode,
						election.getElectionCode()))

				.map(this::mapToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public ElectionResponse getForVoterByCode(String voterCode, String electionCode) {

		Voter voter = voterRepository.findByVoterCode(voterCode)
				.orElseThrow(() -> new RuntimeException("Voter not found"));

		String orgCode = voter.getOrganization().getOrgCode();

		Election election = electionRepository.findByElectionCodeAndOrganization_OrgCode(electionCode, orgCode)
				.orElseThrow(() -> new RuntimeException("Election not found"));

		return mapToResponse(election);
	}

	private ElectionResponse mapToResponse(Election election) {

		return ElectionResponse.builder().electionCode(election.getElectionCode()).name(election.getName())
				.startTime(election.getStartTime()).endTime(election.getEndTime()).isActive(election.getIsActive())
				.orgCode(election.getOrganization().getOrgCode()).candidateCount(election.getCandidates().size())
				.build();
	}
}