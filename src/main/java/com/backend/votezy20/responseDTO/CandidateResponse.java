package com.backend.votezy20.responseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateResponse {

    private String candidateCode;
    private String name;
    private String email;
    private String partyName;
    private String partySymbolUrl;
    private Boolean isActive;
    private String electionCode;
}