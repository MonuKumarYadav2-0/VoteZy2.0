package com.backend.votezy20.responseDTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionResultResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String electionCode;
    private String electionName;
    private String orgCode;
    private Integer totalVotes;

    private String winnerName;
    private String winnerCandidateCode;
    private String winnerPartyName;

    private List<CandidateVoteCountResponse> candidateResults;

    private LocalDateTime announcedAt;
}