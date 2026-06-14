package com.backend.votezy20.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollCandidateRequest {

    @NotBlank(message = "Voter code is required")
    private String voterCode;

    @NotBlank(message = "Election code is required")
    private String electionCode;

    @NotBlank(message = "Party name is required")
    private String partyName;

    private String partySymbolUrl;
}