package com.example.fitness.dto;

import com.example.fitness.model.ContractModel;
import com.example.fitness.model.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberRequest {

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

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    private Gender gender;

    @NotNull
    private ContractModel contractModel;

    @NotNull
    @FutureOrPresent
    private LocalDate contractStart;
}