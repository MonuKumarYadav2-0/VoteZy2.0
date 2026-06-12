package com.backend.votezy20.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.votezy20.entitiy.Organization;

@Repository
public interface OrgRepository extends JpaRepository<Organization, Long> {

	// Login
	Optional<Organization> findByEmail(String email);

	// Profile / JWT auth
	Optional<Organization> findByOrgCode(String orgCode);

	// Registration validation
	boolean existsByEmail(String email);

	boolean existsByOrgCode(String orgCode);
}