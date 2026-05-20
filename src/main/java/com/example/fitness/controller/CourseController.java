package com.example.fitness.controller;

import com.example.fitness.dto.CourseRequest;
import com.example.fitness.dto.CourseResponse;
import com.example.fitness.dto.RegistrationResponse;
import com.example.fitness.model.Member;
import com.example.fitness.model.User;
import com.example.fitness.repository.MemberRepository;
import com.example.fitness.repository.UserRepository;
import com.example.fitness.service.CourseRegistrationService;
import com.example.fitness.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseRegistrationService registrationService;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;


    @GetMapping("/schedule")
    public ResponseEntity<List<CourseResponse>> getSchedule() {
        return ResponseEntity.ok(courseService.getSchedule());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }


    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }


    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CourseResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.cancel(id));
    }


    @PostMapping("/{id}/register")
    public ResponseEntity<RegistrationResponse> register(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = resolveMemberId(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.register(id, memberId));
    }


    @DeleteMapping("/{id}/register")
    public ResponseEntity<RegistrationResponse> cancelRegistration(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = resolveMemberId(userDetails);
        return ResponseEntity.ok(registrationService.cancel(id, memberId));
    }


    private Long resolveMemberId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
        Member member = memberRepository.findAll().stream()
                .filter(m -> m.getUser() != null && m.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new com.example.fitness.exception.BusinessException("No member profile linked to this account."));
        return member.getId();
    }
}