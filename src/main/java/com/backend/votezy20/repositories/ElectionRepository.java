package com.backend.votezy20.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.entitiy.Organization;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

	// Get by election code
	Optional<Election> findByElectionCode(String electionCode);

	// Validation
	boolean existsByElectionCode(String electionCode);

	// Org-specific election
	Optional<Election> findByElectionCodeAndOrganization_OrgCode(String electionCode, String orgCode);

	// Get all elections of org
	List<Election> findByOrganization_OrgCode(String orgCode);

	// Filter by active/inactive
	List<Election> findByOrganization_OrgCodeAndIsActive(String orgCode, Boolean isActive);

	// Active elections
	List<Election> findByIsActiveTrue();

	// Currently running elections
	List<Election> findByStartTimeBeforeAndEndTimeAfter(LocalDateTime now1, LocalDateTime now2);

	// Analytics
	long countByOrganization_OrgCode(String orgCode);

	long countByOrganization_OrgCodeAndIsActive(String orgCode, Boolean isActive);

	// Optional helper
	List<Election> findByOrganization(Organization organization);
}