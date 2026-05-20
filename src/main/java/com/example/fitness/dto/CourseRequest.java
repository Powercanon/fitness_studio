package com.example.fitness.dto;

import com.example.fitness.model.CourseCategory;
import com.example.fitness.model.CourseDifficulty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CourseRequest {

    @NotBlank
    @Size(max = 80)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private Long trainerId;

    @NotNull
    private CourseCategory category;

    @NotNull
    private CourseDifficulty difficulty;

    @NotNull
    @Min(1)
    @Max(200)
    private Integer maxParticipants;

    @NotNull
    @FutureOrPresent
    private LocalDate date;

    @NotNull
    private LocalTime timeFrom;

    @NotNull
    private LocalTime timeUntil;
}