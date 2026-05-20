package com.example.fitness.dto;

import com.example.fitness.model.ContractModel;
import com.example.fitness.model.Gender;
import com.example.fitness.model.MemberStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberResponse {
    private Long id;
    private String memberNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Gender gender;
    private ContractModel contractModel;
    private LocalDate contractStart;
    private MemberStatus status;
}