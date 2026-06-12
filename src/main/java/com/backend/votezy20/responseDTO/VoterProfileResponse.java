package com.backend.votezy20.responseDTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoterProfileResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String voterCode;
    private String name;
    private String email;
    private Boolean isActive;
    private Boolean isPasswordSet;
    private LocalDateTime createdAt;
    private String orgCode;
}