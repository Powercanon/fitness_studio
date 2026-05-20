package com.example.fitness.repository;

import com.example.fitness.model.ContractModel;
import com.example.fitness.model.Gender;
import com.example.fitness.model.Member;
import com.example.fitness.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberNumber(String memberNumber);

    boolean existsByEmail(String email);

    @Query("""
            SELECT m FROM Member m
            WHERE (:status IS NULL OR m.status = :status)
              AND (:gender IS NULL OR m.gender = :gender)
              AND (:contractModel IS NULL OR m.contractModel = :contractModel)
              AND (:contractFrom IS NULL OR m.contractStart >= :contractFrom)
              AND (:contractTo IS NULL OR m.contractStart <= :contractTo)
              AND (:search IS NULL OR :search = ''
                   OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(m.email) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR m.memberNumber LIKE CONCAT('%', :search, '%'))
            """)
    List<Member> findFiltered(
            @Param("status") MemberStatus status,
            @Param("gender") Gender gender,
            @Param("contractModel") ContractModel contractModel,
            @Param("contractFrom") LocalDate contractFrom,
            @Param("contractTo") LocalDate contractTo,
            @Param("search") String search);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(m.memberNumber, 8) AS int)), 0) FROM Member m WHERE m.memberNumber LIKE :prefix%")
    int findMaxSequenceForYear(@Param("prefix") String prefix);
}