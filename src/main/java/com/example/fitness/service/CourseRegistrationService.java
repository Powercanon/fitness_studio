package com.example.fitness.service;

import com.example.fitness.dto.RegistrationResponse;
import com.example.fitness.model.CourseStatus;
import com.example.fitness.model.RegistrationStatus;
import com.example.fitness.exception.BusinessException;
import com.example.fitness.exception.ResourceNotFoundException;
import com.example.fitness.model.Course;
import com.example.fitness.model.CourseRegistration;
import com.example.fitness.model.Member;
import com.example.fitness.repository.CourseRegistrationRepository;
import com.example.fitness.repository.CourseRepository;
import com.example.fitness.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseRegistrationService {

    private final CourseRegistrationRepository registrationRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RegistrationResponse register(Long courseId, Long memberId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));

        if (course.getStatus() == CourseStatus.CANCELLED) {
            throw new BusinessException("Cannot register for a cancelled course.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found."));

        boolean alreadyRegistered = registrationRepository
                .existsByCourseIdAndMemberIdAndStatusNot(courseId, memberId, RegistrationStatus.CANCELLED);
        if (alreadyRegistered) {
            throw new BusinessException("Already registered for this course.");
        }

        int registered = registrationRepository.countRegistered(courseId);
        RegistrationStatus status = registered < course.getMaxParticipants()
                ? RegistrationStatus.REGISTERED
                : RegistrationStatus.WAITLIST;

        CourseRegistration reg = CourseRegistration.builder()
                .course(course)
                .member(member)
                .status(status)
                .build();

        return toResponse(registrationRepository.save(reg));
    }

    @Transactional
    public RegistrationResponse cancel(Long courseId, Long memberId) {
        CourseRegistration reg = registrationRepository
                .findByCourseIdAndMemberId(courseId, memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found."));

        if (reg.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BusinessException("Registration is already cancelled.");
        }

        boolean wasRegistered = reg.getStatus() == RegistrationStatus.REGISTERED;
        reg.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(reg);

        if (wasRegistered) {
            List<CourseRegistration> waitlist = registrationRepository
                    .findWaitlistByCourseOrderByDate(courseId);
            if (!waitlist.isEmpty()) {
                CourseRegistration promoted = waitlist.getFirst();
                promoted.setStatus(RegistrationStatus.REGISTERED);
                registrationRepository.save(promoted);
            }
        }

        return toResponse(reg);
    }

    public List<RegistrationResponse> getByMember(Long memberId) {
        return registrationRepository.findByMemberId(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private RegistrationResponse toResponse(CourseRegistration r) {
        RegistrationResponse res = new RegistrationResponse();
        res.setId(r.getId());
        res.setStatus(r.getStatus());
        res.setRegisteredAt(r.getRegisteredAt());

        if (r.getCourse() != null) {
            res.setCourseId(r.getCourse().getId());
            res.setCourseName(r.getCourse().getName());
        }
        if (r.getMember() != null) {
            res.setMemberId(r.getMember().getId());
            res.setMemberFullName(r.getMember().getFirstName() + " " + r.getMember().getLastName());
        }

        return res;
    }
}