package com.skillcert.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String password;
    private String phone;
    private String company;

    // Agrega este campo:
    @Pattern(regexp = "ADMIN|NORMAL|INSTRUCTOR")
    private String role;
}
