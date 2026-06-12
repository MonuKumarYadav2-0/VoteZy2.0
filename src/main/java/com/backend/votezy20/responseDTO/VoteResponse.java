package com.backend.votezy20.responseDTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long voteId;
    private String voterCode;
    private String candidateCode;
    private String electionCode;
    private LocalDateTime votedAt;
}