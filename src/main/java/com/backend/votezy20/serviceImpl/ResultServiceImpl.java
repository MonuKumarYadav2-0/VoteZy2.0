package com.backend.votezy20.serviceImpl;

import java.util.Comparator;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.votezy20.entitiy.Candidate;
import com.backend.votezy20.entitiy.Election;
import com.backend.votezy20.entitiy.ElectionResult;
import com.backend.votezy20.repositories.ElectionRepository;
import com.backend.votezy20.repositories.ElectionResultRepository;
import com.backend.votezy20.repositories.OrgRepository;
import com.backend.votezy20.repositories.VoteRepository;
import com.backend.votezy20.responseDTO.CandidateVoteCountResponse;
import com.backend.votezy20.responseDTO.ElectionResultResponse;
import com.backend.votezy20.service.ResultService;

import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ResultServiceImpl
        implements ResultService {

    private final ElectionRepository electionRepository;
    private final ElectionResultRepository resultRepository;
    private final VoteRepository voteRepository;
    private final OrgRepository orgRepository;
    private final ObjectMapper objectMapper;

    @Override
    @CacheEvict(
            value = "results",
            allEntries = true
    )
    public ElectionResultResponse announce(
            String orgCode,
            String electionCode
    ) {

        orgRepository
                .findByOrgCode(orgCode)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Organization not found"
                                )
                );

        if (resultRepository
                .existsByElectionCode(
                        electionCode
                )) {

            throw new RuntimeException(
                    "Result already announced"
            );
        }

        Election election =
                electionRepository
                        .findByElectionCodeAndOrganization_OrgCode(
                                electionCode,
                                orgCode
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Election not found"
                                        )
                        );

        List<CandidateVoteCountResponse>
                candidateResults =
                election.getCandidates()
                        .stream()
                        .map(candidate ->
                                buildCandidateResult(
                                        electionCode,
                                        candidate
                                )
                        )
                        .sorted(
                                Comparator
                                        .comparing(
                                                CandidateVoteCountResponse
                                                        ::getVoteCount
                                        )
                                        .reversed()
                        )
                        .toList();

        CandidateVoteCountResponse
                winner =
                candidateResults
                        .stream()
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "No candidates found"
                                        )
                        );

        String candidateResultsJson;

        try {

            candidateResultsJson =
                    objectMapper
                            .writeValueAsString(
                                    candidateResults
                            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to serialize result"
            );
        }

        ElectionResult result =
                ElectionResult
                        .builder()
                        .electionCode(
                                election.getElectionCode()
                        )
                        .electionName(
                                election.getName()
                        )
                        .organizationCode(
                                orgCode
                        )
                        .organization(
                                election.getOrganization()
                        )
                        .totalVotes(
                                (int)
                                voteRepository
                                        .countByElection_ElectionCode(
                                                electionCode
                                        )
                        )
                        .winnerName(
                                winner.getName()
                        )
                        .winnerCandidateCode(
                                winner.getCandidateCode()
                        )
                        .winnerPartyName(
                                winner.getPartyName()
                        )
                        .candidateResultsJson(
                                candidateResultsJson
                        )
                        .build();

        resultRepository.save(result);

        return mapToResponse(result);
    }

    @Override
    @Cacheable(
            value = "results",
            key = "#electionCode"
    )
    @Transactional(readOnly = true)
    public ElectionResultResponse
    getResult(
            String electionCode
    ) {

        ElectionResult result =
                resultRepository
                        .findByElectionCode(
                                electionCode
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Result not found"
                                        )
                        );

        return mapToResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElectionResultResponse>
    getAllResults(
            String orgCode
    ) {

        return resultRepository
                .findByOrganization_OrgCode(
                        orgCode
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @CacheEvict(
            value = "results",
            allEntries = true
    )
    public void deleteResult(
            String orgCode,
            String electionCode
    ) {

        ElectionResult result =
                resultRepository
                        .findByElectionCodeAndOrganization_OrgCode(
                                electionCode,
                                orgCode
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Result not found"
                                        )
                        );

        resultRepository.delete(result);
    }

    private CandidateVoteCountResponse
    buildCandidateResult(
            String electionCode,
            Candidate candidate
    ) {

        long voteCount =
                voteRepository
                        .countByElection_ElectionCodeAndCandidate_CandidateCode(
                                electionCode,
                                candidate.getCandidateCode()
                        );

        return CandidateVoteCountResponse
                .builder()
                .candidateCode(
                        candidate.getCandidateCode()
                )
                .name(
                        candidate.getName()
                )
                .partyName(
                        candidate.getPartyName()
                )
                .voteCount(
                        voteCount
                )
                .build();
    }

    private ElectionResultResponse
    mapToResponse(
            ElectionResult result
    ) {

        List<CandidateVoteCountResponse>
                candidateResults;

        try {

            candidateResults =
                    objectMapper
                            .readValue(
                                    result.getCandidateResultsJson(),
                                    new TypeReference<
                                            List<
                                                    CandidateVoteCountResponse
                                                    >
                                            >() {
                                    }
                            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to deserialize result"
            );
        }

        return ElectionResultResponse
                .builder()
                .electionCode(
                        result.getElectionCode()
                )
                .electionName(
                        result.getElectionName()
                )
                .orgCode(
                        result.getOrganizationCode()
                )
                .totalVotes(
                        result.getTotalVotes()
                )
                .winnerName(
                        result.getWinnerName()
                )
                .winnerCandidateCode(
                        result.getWinnerCandidateCode()
                )
                .winnerPartyName(
                        result.getWinnerPartyName()
                )
                .candidateResults(
                        candidateResults
                )
                .announcedAt(
                        result.getAnnouncedAt()
                )
                .build();
    }
}