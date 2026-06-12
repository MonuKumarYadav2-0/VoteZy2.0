package com.backend.votezy20.serviceImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.repositories.ElectionRepository;
import com.backend.votezy20.repositories.OrgRepository;
import com.backend.votezy20.repositories.VoteRepository;
import com.backend.votezy20.repositories.VoterRepository;
import com.backend.votezy20.responseDTO.AnalyticsResponse;
import com.backend.votezy20.responseDTO.ElectionResponse;
import com.backend.votezy20.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

	private final OrgRepository orgRepository;
	private final ElectionRepository electionRepository;
	private final VoterRepository voterRepository;
	private final VoteRepository voteRepository;

	@Override
	public AnalyticsResponse getAnalytics(String orgCode) {

		orgRepository.findByOrgCode(orgCode).orElseThrow(() -> new RuntimeException("Organization not found"));

		long totalElections = electionRepository.countByOrganization_OrgCode(orgCode);

		long activeElections = electionRepository.countByOrganization_OrgCodeAndIsActive(orgCode, true);

		List<Election> allElections = electionRepository.findByOrganization_OrgCode(orgCode);

		long completedElections = allElections.stream()
				.filter(election -> election.getEndTime().isBefore(LocalDateTime.now())).count();

		long totalVoters = voterRepository
				.findByOrganization_OrgCode(orgCode, org.springframework.data.domain.Pageable.unpaged())
				.getTotalElements();

		long totalVotesCast = allElections.stream()
				.mapToLong(election -> voteRepository.countByElection_ElectionCode(election.getElectionCode())).sum();

		List<ElectionResponse> recentElections = allElections.stream()
				.sorted(Comparator.comparing(Election::getStartTime).reversed()).limit(5).map(this::mapToResponse)
				.toList();

		return AnalyticsResponse.builder().totalElections(totalElections).totalVoters(totalVoters)
				.totalVotesCast(totalVotesCast).activeElections(activeElections).completedElections(completedElections)
				.recentElections(recentElections).build();
	}

	private ElectionResponse mapToResponse(Election election) {

		return ElectionResponse.builder().electionCode(election.getElectionCode()).name(election.getName())
				.startTime(election.getStartTime()).endTime(election.getEndTime()).isActive(election.getIsActive())
				.orgCode(election.getOrganization().getOrgCode()).candidateCount(election.getCandidates().size())
				.build();
	}
}