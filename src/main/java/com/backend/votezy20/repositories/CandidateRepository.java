package com.backend.votezy20.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	// Candidate profile
	Optional<Candidate> findByCandidateCode(String candidateCode);

	// Validation
	boolean existsByCandidateCode(String candidateCode);

	// Org ownership check
	Optional<Candidate> findByCandidateCodeAndOrganization_OrgCode(String candidateCode, String orgCode);

	// All candidates of org
	List<Candidate> findByOrganization_OrgCode(String orgCode);

	// Active candidates
	List<Candidate> findByOrganization_OrgCodeAndIsActiveTrue(String orgCode);
}