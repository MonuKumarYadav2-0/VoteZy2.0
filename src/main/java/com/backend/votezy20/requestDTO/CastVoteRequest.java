package com.backend.votezy20.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CastVoteRequest {

    @NotBlank(message = "Candidate code is required")
    private String candidateCode;

    @NotBlank(message = "Election code is required")
    private String electionCode;
}