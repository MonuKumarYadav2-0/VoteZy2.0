package com.backend.votezy20.responseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateVoteCountResponse {

    private String candidateCode;
    private String name;
    private String partyName;
    private Long voteCount;
}