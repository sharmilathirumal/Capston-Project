package com.lmsProject.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.InstructorDeactivation;


public interface InstructorDeactivationRepository extends JpaRepository<InstructorDeactivation,Long>{
    @Query("""
                SELECT ind
                FROM InstructorDeactivation ind
                JOIN FETCH ind.instructor i
                WHERE i.active = false
                  AND (
                    LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(i.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
            """)
    List<InstructorDeactivation> searchDeactivatedInstructor(@Param("keyword") String keyword);
}
