package com.example.fitness.dto;

import com.example.fitness.model.RegistrationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationResponse {
    private Long id;
    private Long courseId;
    private String courseName;
    private Long memberId;
    private String memberFullName;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;
}