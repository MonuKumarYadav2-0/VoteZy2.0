package com.backend.votezy20.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.Candidate;
import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.entitiy.Vote;
import com.backend.votezy20.entitiy.Voter;
import com.backend.votezy20.repositories.CandidateRepository;
import com.backend.votezy20.repositories.ElectionRepository;
import com.backend.votezy20.repositories.VoteRepository;
import com.backend.votezy20.repositories.VoterRepository;
import com.backend.votezy20.requestDTO.CastVoteRequest;
import com.backend.votezy20.responseDTO.CandidateVoteCountResponse;
import com.backend.votezy20.responseDTO.VoteResponse;
import com.backend.votezy20.service.VoteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteServiceImpl implements VoteService {

	private final VoteRepository voteRepository;
	private final VoterRepository voterRepository;
	private final ElectionRepository electionRepository;
	private final CandidateRepository candidateRepository;
	private final SimpMessagingTemplate messagingTemplate;

	@Override
	@Transactional
	@CacheEvict(value = { "voteCount", "results" }, allEntries = true)
	public void castVote(String voterCode, CastVoteRequest request) {

		Voter voter = voterRepository.findByVoterCode(voterCode)
				.orElseThrow(() -> new RuntimeException("Voter not found"));

		if (!voter.getIsActive()) {

			throw new RuntimeException("Voter account inactive");
		}

		if (!voter.getIsPasswordSet()) {

			throw new RuntimeException("Password not setup");
		}

		Election election = electionRepository.findByElectionCode(request.getElectionCode())
				.orElseThrow(() -> new RuntimeException("Election not found"));

		// Election active?
		if (!election.getIsActive()) {

			throw new RuntimeException("Election inactive");
		}

		LocalDateTime now = LocalDateTime.now();

		// Election started?
		if (now.isBefore(election.getStartTime())) {

			throw new RuntimeException("Election has not started");
		}

		// Election ended?
		if (now.isAfter(election.getEndTime())) {

			throw new RuntimeException("Election ended");
		}

		// Already voted?
		boolean alreadyVoted = voteRepository.existsByVoter_VoterCodeAndElection_ElectionCode(voterCode,
				request.getElectionCode());

		if (alreadyVoted) {

			throw new RuntimeException("Already voted in this election");
		}

		Candidate candidate = candidateRepository.findByCandidateCode(request.getCandidateCode())
				.orElseThrow(() -> new RuntimeException("Candidate not found"));

		// Candidate belongs to election?
		boolean candidateInElection = election.getCandidates().stream()
				.anyMatch(c -> c.getCandidateCode().equals(candidate.getCandidateCode()));

		if (!candidateInElection) {

			throw new RuntimeException("Candidate does not belong to election");
		}

		Vote vote = Vote.builder().voter(voter).candidate(candidate).election(election).build();

		voteRepository.save(vote);

		// Live vote count broadcast
		List<CandidateVoteCountResponse> liveData = election.getCandidates().stream()
				.map(c -> CandidateVoteCountResponse.builder().candidateCode(c.getCandidateCode()).name(c.getName())
						.partyName(c.getPartyName())
						.voteCount(voteRepository.countByElection_ElectionCodeAndCandidate_CandidateCode(
								election.getElectionCode(), c.getCandidateCode()))
						.build())
				.toList();

		messagingTemplate.convertAndSend("/topic/results/" + election.getElectionCode(), liveData);
		return;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasVoted(String voterCode, String electionCode) {

		return voteRepository.existsByVoter_VoterCodeAndElection_ElectionCode(voterCode, electionCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VoteResponse> getByElection(String orgCode, String electionCode) {

		List<Vote> votes = voteRepository.findByElection_ElectionCodeAndElection_Organization_OrgCode(electionCode,
				orgCode);

		return votes.stream()
				.map(vote -> VoteResponse.builder().voteId(vote.getId()).voterCode(vote.getVoter().getVoterCode())
						.candidateCode(vote.getCandidate().getCandidateCode())
						.electionCode(vote.getElection().getElectionCode()).votedAt(vote.getVotedAt()).build())
				.toList();
	}

	@Override
	public VoteResponse getMyVote(String voterCode, String electionCode) {
		// TODO Auto-generated method stub
		return null;
	}
}