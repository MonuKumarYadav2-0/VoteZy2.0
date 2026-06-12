package com.backend.votezy20.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrgProfileRequest {

    @NotBlank(message = "Organization name is required")
    private String orgName;

    private String address;
}