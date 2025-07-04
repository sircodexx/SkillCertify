package com.skillcert.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String name;
    private String email;
    private String role;

    public AuthResponse(String accessToken, String refreshToken, String tokenType, String name, String email, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
