package com.backend.votezy20.requestDTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateElectionRequest {

    @NotBlank(
            message = "Election name is required"
    )
    private String name;

    @NotNull(
            message = "Start time is required"
    )
    @Future(
            message = "Start time must be in future"
    )
    private LocalDateTime startTime;

    @NotNull(
            message = "End time is required"
    )
    @Future(
            message = "End time must be in future"
    )
    private LocalDateTime endTime;

    private Boolean isActive;
}