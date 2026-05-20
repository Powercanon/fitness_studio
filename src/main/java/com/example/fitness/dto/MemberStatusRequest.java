package com.example.fitness.dto;

import com.example.fitness.model.MemberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberStatusRequest {
    @NotNull
    private MemberStatus status;
}