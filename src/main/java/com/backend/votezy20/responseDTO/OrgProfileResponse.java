package com.backend.votezy20.responseDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgProfileResponse {

    private String orgCode;
    private String orgName;
    private String email;
    private String address;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}