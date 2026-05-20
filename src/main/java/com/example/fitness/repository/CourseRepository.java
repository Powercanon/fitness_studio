package com.example.fitness.repository;

import com.example.fitness.model.Course;
import com.example.fitness.model.CourseCategory;
import com.example.fitness.model.CourseDifficulty;
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
            SELECT c FROM Course c
            LEFT JOIN FETCH c.trainer t
            WHERE (:status IS NULL OR c.status = :status)
              AND (:category IS NULL OR c.category = :category)
              AND (:difficulty IS NULL OR c.difficulty = :difficulty)
              AND (:trainerId IS NULL OR t.id = :trainerId)
              AND (:dateFrom IS NULL OR c.date >= :dateFrom)
              AND (:dateTo IS NULL OR c.date <= :dateTo)
              AND (:search IS NULL OR :search = ''
                   OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    List<Course> findFiltered(
            @Param("status") CourseStatus status,
            @Param("category") CourseCategory category,
            @Param("difficulty") CourseDifficulty difficulty,
            @Param("trainerId") Long trainerId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("search") String search);

    @Query("""
            SELECT COUNT(r) FROM CourseRegistration r
            WHERE r.course.id = :courseId AND r.status = 'REGISTERED'
            """)
    int countRegisteredParticipants(@Param("courseId") Long courseId);
}