package com.example.fitness.controller;

import com.example.fitness.dto.TrainerRequest;
import com.example.fitness.dto.TrainerResponse;
import com.example.fitness.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;


    @GetMapping
    public ResponseEntity<List<TrainerResponse>> getAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "lastName") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(trainerService.getAll(active, search, sortBy, sortDir));
    }


    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.getById(id));
    }


    @PostMapping
    public ResponseEntity<TrainerResponse> create(@Valid @RequestBody TrainerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.create(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TrainerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TrainerRequest request) {
        return ResponseEntity.ok(trainerService.update(id, request));
    }


    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<TrainerResponse> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(trainerService.toggleActive(id));
    }
}