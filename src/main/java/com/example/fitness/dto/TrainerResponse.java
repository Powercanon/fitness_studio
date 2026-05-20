package com.example.fitness.dto;

import lombok.Data;

@Data
public class TrainerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean active;
}