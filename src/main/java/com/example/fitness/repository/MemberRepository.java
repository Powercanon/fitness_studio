package com.example.fitness.repository;

import com.example.fitness.model.Member;
import com.example.fitness.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberNumber(String memberNumber);

    boolean existsByEmail(String email);

    @Query("""
            SELECT m FROM Member m
            WHERE (:status IS NULL OR m.status = :status)
              AND (:search IS NULL OR :search = ''
                   OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR m.memberNumber LIKE CONCAT('%', :search, '%'))
            ORDER BY m.lastName, m.firstName
            """)
    List<Member> findByStatusAndSearch(@Param("status") MemberStatus status,
                                       @Param("search") String search);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(m.memberNumber, 8) AS int)), 0) FROM Member m WHERE m.memberNumber LIKE :prefix%")
    int findMaxSequenceForYear(@Param("prefix") String prefix);
}