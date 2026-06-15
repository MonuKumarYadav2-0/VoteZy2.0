package com.backend.votezy20.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.repositories.ElectionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectionScheduler {

	private final ElectionRepository electionRepository;

	@Scheduled(fixedRate = 60000000) // every 1 minute
	@Transactional
	public void deactivateExpiredElections() {

		List<Election> expiredElections = electionRepository.findByIsActiveTrueAndEndTimeBefore(LocalDateTime.now());

		if (expiredElections.isEmpty()) {
			return;
		}

		expiredElections.forEach(election -> election.setIsActive(false));

		electionRepository.saveAll(expiredElections);

		log.info("{} elections deactivated automatically", expiredElections.size());
	}
}