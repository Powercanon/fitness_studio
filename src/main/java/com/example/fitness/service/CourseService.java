package com.example.fitness.service;

import com.example.fitness.dto.CourseRequest;
import com.example.fitness.dto.CourseResponse;
import com.example.fitness.model.CourseStatus;
import com.example.fitness.exception.BusinessException;
import com.example.fitness.exception.ResourceNotFoundException;
import com.example.fitness.model.Course;
import com.example.fitness.model.Trainer;
import com.example.fitness.repository.CourseRepository;
import com.example.fitness.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TrainerRepository trainerRepository;

    public List<CourseResponse> getSchedule() {
        return courseRepository.findUpcomingByStatus(CourseStatus.ACTIVE, LocalDate.now())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CourseResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public CourseResponse create(CourseRequest request) {
        validateTimes(request);
        Trainer trainer = findTrainerOrThrow(request.getTrainerId());

        Course course = new Course();
        applyRequest(course, request, trainer);
        course.setStatus(CourseStatus.ACTIVE);

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        validateTimes(request);
        Course course = findOrThrow(id);
        Trainer trainer = findTrainerOrThrow(request.getTrainerId());
        applyRequest(course, request, trainer);
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse cancel(Long id) {
        Course course = findOrThrow(id);
        if (course.getStatus() == CourseStatus.CANCELLED) {
            throw new BusinessException("Course is already cancelled.");
        }
        course.setStatus(CourseStatus.CANCELLED);
        return toResponse(courseRepository.save(course));
    }


    private Course findOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
    }

    private Trainer findTrainerOrThrow(Long trainerId) {
        return trainerRepository.findById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found."));
    }

    private void validateTimes(CourseRequest request) {
        if (request.getTimeUntil() != null && request.getTimeFrom() != null
                && !request.getTimeUntil().isAfter(request.getTimeFrom())) {
            throw new BusinessException("time_until must be after time_from.");
        }
    }

    private void applyRequest(Course course, CourseRequest req, Trainer trainer) {
        course.setName(req.getName());
        course.setDescription(req.getDescription());
        course.setTrainer(trainer);
        course.setCategory(req.getCategory());
        course.setDifficulty(req.getDifficulty());
        course.setMaxParticipants(req.getMaxParticipants());
        course.setDate(req.getDate());
        course.setTimeFrom(req.getTimeFrom());
        course.setTimeUntil(req.getTimeUntil());
    }

    CourseResponse toResponse(Course c) {
        int registered = courseRepository.countRegisteredParticipants(c.getId());
        int available = Math.max(0, c.getMaxParticipants() - registered);

        CourseResponse r = new CourseResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setDescription(c.getDescription());
        r.setCategory(c.getCategory());
        r.setDifficulty(c.getDifficulty());
        r.setMaxParticipants(c.getMaxParticipants());
        r.setRegisteredCount(registered);
        r.setAvailableSpots(available);
        r.setDate(c.getDate());
        r.setTimeFrom(c.getTimeFrom());
        r.setTimeUntil(c.getTimeUntil());
        r.setStatus(c.getStatus());

        if (c.getTrainer() != null) {
            r.setTrainerId(c.getTrainer().getId());
            r.setTrainerFullName(c.getTrainer().getFirstName() + " " + c.getTrainer().getLastName());
        }

        return r;
    }
}