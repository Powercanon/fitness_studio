package com.example.fitness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CapacityResponse {
    private int currentCount;
    private int maxCapacity;
    private double occupancyPercent;
    private String indicator; // GREEN, YELLOW, RED
    private List<CheckinResponse> currentlyCheckedIn;
}