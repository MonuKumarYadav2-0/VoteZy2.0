package com.backend.votezy20.responseDTO;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgProfileResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orgCode;
    private String orgName;
    private String email;
    private String address;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}