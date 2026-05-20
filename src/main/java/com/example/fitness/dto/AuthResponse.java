package com.example.fitness.dto;

import com.example.fitness.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserRole role;
    private String email;
}