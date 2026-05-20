package com.example.fitness.controller;

import com.example.fitness.dto.CapacityResponse;
import com.example.fitness.dto.CheckinRequest;
import com.example.fitness.dto.CheckinResponse;
import com.example.fitness.service.CheckinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;
    @GetMapping("/capacity")
    public ResponseEntity<CapacityResponse> getCapacity() {
        return ResponseEntity.ok(checkinService.getCapacity());
    }

    @PostMapping
    public ResponseEntity<CheckinResponse> checkIn(@Valid @RequestBody CheckinRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkinService.checkIn(request));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<CheckinResponse> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(checkinService.checkOut(id));
    }
}