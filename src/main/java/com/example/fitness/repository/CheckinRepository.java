package com.example.fitness.repository;

import com.example.fitness.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    @Query("SELECT c FROM Checkin c JOIN FETCH c.member WHERE c.checkedOutAt IS NULL ORDER BY c.checkedInAt")
    List<Checkin> findCurrentlyCheckedIn();

    @Query("SELECT COUNT(c) FROM Checkin c WHERE c.checkedOutAt IS NULL")
    int countCurrentlyCheckedIn();

    @Query("SELECT c FROM Checkin c WHERE c.member.id = :memberId AND c.checkedOutAt IS NULL")
    Optional<Checkin> findActiveCheckinByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndCheckedOutAtIsNull(Long memberId);

    @Query("""
            SELECT c FROM Checkin c
            JOIN FETCH c.member m
            WHERE (:checkedOut IS NULL
                   OR (:checkedOut = true AND c.checkedOutAt IS NOT NULL)
                   OR (:checkedOut = false AND c.checkedOutAt IS NULL))
              AND (:dateFrom IS NULL OR c.checkedInAt >= :dateFrom)
              AND (:dateTo IS NULL OR c.checkedInAt <= :dateTo)
              AND (:search IS NULL OR :search = ''
                   OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR m.memberNumber LIKE CONCAT('%', :search, '%'))
            """)
    List<Checkin> findFiltered(
            @Param("checkedOut") Boolean checkedOut,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("search") String search);
}