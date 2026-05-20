package com.example.fitness.repository;

import com.example.fitness.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    List<Trainer> findAllByOrderByLastNameAscFirstNameAsc();
    List<Trainer> findByActiveTrue();

    @Query("""
            SELECT t FROM Trainer t
            WHERE (:active IS NULL OR t.active = :active)
              AND (:search IS NULL OR :search = ''
                   OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(t.email) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    List<Trainer> findFiltered(@Param("active") Boolean active, @Param("search") String search);
}