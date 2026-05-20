package com.example.fitness.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TrainerRequest {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String phone;
}