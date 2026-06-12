package com.backend.votezy20.serviceImpl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.Candidate;
import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.repositories.CandidateRepository;
import com.backend.votezy20.repositories.ElectionRepository;
import com.backend.votezy20.repositories.OrgRepository;
import com.backend.votezy20.requestDTO.EnrollCandidateRequest;
import com.backend.votezy20.responseDTO.CandidateResponse;
import com.backend.votezy20.service.CandidateService;
import com.backend.votezy20.util.CodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateServiceImpl implements CandidateService {

	private final CandidateRepository candidateRepository;
	private final ElectionRepository electionRepository;
	private final OrgRepository orgRepository;

	@Override
	@CacheEvict(value = "candidates", allEntries = true)
	public CandidateResponse enroll(String orgCode, EnrollCandidateRequest request) {

		// Organization exists?
		orgRepository.findByOrgCode(orgCode).orElseThrow(() -> new RuntimeException("Organization not found"));

		// Election exists + ownership
		Election election = electionRepository
				.findByElectionCodeAndOrganization_OrgCode(request.getElectionCode(), orgCode)
				.orElseThrow(() -> new RuntimeException("Election not found"));

		// Find reusable candidate
		Candidate candidate = candidateRepository.findByOrganization_OrgCode(orgCode).stream()
				.filter(c -> c.getEmail().equalsIgnoreCase(request.getEmail())).findFirst().orElse(null);

		// Create new candidate if not exists
		if (candidate == null) {

			candidate = Candidate.builder().candidateCode(CodeGenerator.generateCandidateCode()).name(request.getName())
					.email(request.getEmail()).partyName(request.getPartyName())
					.partySymbolUrl(request.getPartySymbolUrl()).isActive(true).organization(election.getOrganization())
					.build();

			candidate = candidateRepository.save(candidate);
		}

		// Already enrolled?
		if (election.getCandidates().contains(candidate)) {

			throw new RuntimeException("Candidate already enrolled in election");
		}

		// Add candidate to election
		election.getCandidates().add(candidate);

		electionRepository.save(election);

		return mapToResponse(candidate, election.getElectionCode());
	}

	@Override
	@Cacheable(value = "candidates", key = "#electionCode")
	@Transactional(readOnly = true)
	public List<CandidateResponse> getByElection(String electionCode) {

		Election election = electionRepository.findByElectionCode(electionCode)
				.orElseThrow(() -> new RuntimeException("Election not found"));

		return election.getCandidates().stream().map(candidate -> mapToResponse(candidate, electionCode)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public CandidateResponse getByCode(String candidateCode) {

		Candidate candidate = candidateRepository.findByCandidateCode(candidateCode)
				.orElseThrow(() -> new RuntimeException("Candidate not found"));

		String electionCode = candidate.getElections().stream().findFirst().map(Election::getElectionCode).orElse(null);

		return mapToResponse(candidate, electionCode);
	}

	private CandidateResponse mapToResponse(Candidate candidate, String electionCode) {

		return CandidateResponse.builder().candidateCode(candidate.getCandidateCode()).name(candidate.getName())
				.email(candidate.getEmail()).partyName(candidate.getPartyName())
				.partySymbolUrl(candidate.getPartySymbolUrl()).isActive(candidate.getIsActive())
				.electionCode(electionCode).build();
	}
}