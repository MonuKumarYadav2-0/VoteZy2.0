package com.backend.votezy20.responseDTO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable{
    private static final long serialVersionUID = 1L;


    private String token;

    private String role;

    private String code;

    private String name;
}