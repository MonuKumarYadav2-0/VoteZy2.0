package com.backend.votezy20.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.Organization;
import com.backend.votezy20.entitiy.Voter;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {

	// Login
	Optional<Voter> findByEmail(String email);

	// JWT auth / profile
	Optional<Voter> findByVoterCode(String voterCode);

	// Validation while add voter
	boolean existsByEmail(String email);

	boolean existsByVoterCode(String voterCode);

	// Get all voters of an org (paginated)
	Page<Voter> findByOrganization_OrgCode(String orgCode, Pageable pageable);

	// Org ownership check
	Optional<Voter> findByVoterCodeAndOrganization_OrgCode(String voterCode, String orgCode);

	// Optional helper
	List<Voter> findByOrganization(Organization organization);

	
}