package com.example.fitness.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckinResponse {
    private Long id;
    private Long memberId;
    private String memberNumber;
    private String memberFullName;
    private LocalDateTime checkedInAt;
    private LocalDateTime checkedOutAt;
}