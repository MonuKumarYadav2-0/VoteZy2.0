package com.backend.votezy20.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollCandidateRequest {

    @NotBlank(message = "Election code is required")
    private String electionCode;

    @NotBlank(message = "Candidate name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String partyName;

    private String partySymbolUrl;
}