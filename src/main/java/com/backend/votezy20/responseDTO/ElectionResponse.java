package com.backend.votezy20.responseDTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String electionCode;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isActive;
    private String orgCode;
    private Integer candidateCount;
}