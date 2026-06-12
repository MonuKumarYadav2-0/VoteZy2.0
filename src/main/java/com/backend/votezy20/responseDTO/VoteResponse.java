package com.backend.votezy20.responseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResponse {

    private Long voteId;
    private String voterCode;
    private String candidateCode;
    private String electionCode;
    private LocalDateTime votedAt;
}