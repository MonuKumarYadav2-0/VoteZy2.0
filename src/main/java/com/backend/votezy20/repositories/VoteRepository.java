package com.backend.votezy20.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // One voter -> one election vote check
    boolean existsByVoter_VoterCodeAndElection_ElectionCode(
            String voterCode,
            String electionCode
    );

    // Has voted endpoint
    Optional<Vote>
    findByVoter_VoterCodeAndElection_ElectionCode(
            String voterCode,
            String electionCode
    );

    // Votes by election
    List<Vote>
    findByElection_ElectionCode(
            String electionCode
    );

    // Org ownership validation
    List<Vote>
    findByElection_ElectionCodeAndElection_Organization_OrgCode(
            String electionCode,
            String orgCode
    );

    // Vote count
    long countByElection_ElectionCode(
            String electionCode
    );

    long countByCandidate_CandidateCode(
            String candidateCode
    );

    long countByElection_ElectionCodeAndCandidate_CandidateCode(
            String electionCode,
            String candidateCode
    );
}