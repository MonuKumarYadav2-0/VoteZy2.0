package com.backend.votezy20.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.ElectionResult;

@Repository
public interface ElectionResultRepository extends JpaRepository<ElectionResult, Long> {

	// Get single result
	Optional<ElectionResult> findByElectionCode(String electionCode);

	// Validation
	boolean existsByElectionCode(String electionCode);

	// All results of org
	List<ElectionResult> findByOrganization_OrgCode(String orgCode);

	// Delete result
	void deleteByElectionCode(String electionCode);

	Optional<ElectionResult> findByElectionCodeAndOrganization_OrgCode(String electionCode, String orgCode);
}