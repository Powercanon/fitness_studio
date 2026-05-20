package com.example.fitness.repository;

import com.example.fitness.model.CourseRegistration;
import com.example.fitness.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {

    Optional<CourseRegistration> findByCourseIdAndMemberId(Long courseId, Long memberId);

    boolean existsByCourseIdAndMemberIdAndStatusNot(Long courseId, Long memberId, RegistrationStatus status);

    @Query("SELECT COUNT(r) FROM CourseRegistration r WHERE r.course.id = :courseId AND r.status = 'REGISTERED'")
    int countRegistered(@Param("courseId") Long courseId);

    @Query("""
            SELECT r FROM CourseRegistration r
            WHERE r.course.id = :courseId AND r.status = 'WAITLIST'
            ORDER BY r.registeredAt ASC
            """)
    List<CourseRegistration> findWaitlistByCourseOrderByDate(@Param("courseId") Long courseId);

    List<CourseRegistration> findByMemberId(Long memberId);
}