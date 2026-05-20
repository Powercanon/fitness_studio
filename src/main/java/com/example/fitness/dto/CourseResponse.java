package com.example.fitness.dto;

import com.example.fitness.model.CourseCategory;
import com.example.fitness.model.CourseDifficulty;
import com.example.fitness.model.CourseStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CourseResponse {
    private Long id;
    private String name;
    private String description;
    private Long trainerId;
    private String trainerFullName;
    private CourseCategory category;
    private CourseDifficulty difficulty;
    private int maxParticipants;
    private int registeredCount;
    private int availableSpots;
    private LocalDate date;
    private LocalTime timeFrom;
    private LocalTime timeUntil;
    private CourseStatus status;
}