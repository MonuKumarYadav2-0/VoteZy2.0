package com.backend.votezy20.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionStatusRequest {

    @NotBlank(message = "Election code is required")
    private String electionCode;

    @NotNull(message = "Active status is required")
    private Boolean active;
}