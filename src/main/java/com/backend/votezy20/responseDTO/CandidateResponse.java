package com.backend.votezy20.responseDTO;

import java.io.Serializable;

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
public class CandidateResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String candidateCode;
    private String name;
    private String email;
    private String partyName;
    private String partySymbolUrl;
    private Boolean isActive;
    private String electionCode;
}