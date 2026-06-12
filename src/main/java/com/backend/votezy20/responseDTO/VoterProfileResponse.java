package com.backend.votezy20.responseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoterProfileResponse {

    private String voterCode;
    private String name;
    private String email;
    private Boolean isActive;
    private Boolean isPasswordSet;
    private LocalDateTime createdAt;
    private String orgCode;
}