package com.backend.votezy20.responseDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {

    private Long totalElections;
    private Long totalVoters;
    private Long totalVotesCast;
    private Long activeElections;
    private Long completedElections;

    private List<ElectionResponse> recentElections;
}