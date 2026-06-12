package com.backend.votezy20.responseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionResponse {

    private String electionCode;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isActive;
    private String orgCode;
    private Integer candidateCount;
}