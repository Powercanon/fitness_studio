package com.example.fitness.repository;

import com.example.fitness.model.Course;
import com.example.fitness.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
            SELECT c FROM Course c
            LEFT JOIN FETCH c.trainer
            WHERE c.status = :status AND c.date >= :fromDate
            ORDER BY c.date, c.timeFrom
            """)
    List<Course> findUpcomingByStatus(@Param("status") CourseStatus status,
                                      @Param("fromDate") LocalDate fromDate);

    @Query("""
            SELECT COUNT(r) FROM CourseRegistration r
            WHERE r.course.id = :courseId AND r.status = 'REGISTERED'
            """)
    int countRegisteredParticipants(@Param("courseId") Long courseId);
}