package com.example.fitness.controller;

import com.example.fitness.dto.CapacityResponse;
import com.example.fitness.dto.CheckinRequest;
import com.example.fitness.dto.CheckinResponse;
import com.example.fitness.service.CheckinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    @GetMapping
    public ResponseEntity<List<CheckinResponse>> getAll(
            @RequestParam(required = false) Boolean checkedOut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "checkedInAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(checkinService.getAll(checkedOut, dateFrom, dateTo, search, sortBy, sortDir));
    }

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