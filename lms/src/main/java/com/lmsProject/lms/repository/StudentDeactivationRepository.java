package com.lmsProject.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.entity.StudentDeactivation;
import java.util.List;

public interface StudentDeactivationRepository extends JpaRepository<StudentDeactivation, Long> {

    StudentDeactivation findByStudent(Student student);

    @Query("SELECT sd FROM StudentDeactivation sd JOIN FETCH sd.student s WHERE s.active = false")
    List<StudentDeactivation> findAllDeactivatedStudents();

    @Query("""
                SELECT sd
                FROM StudentDeactivation sd
                JOIN FETCH sd.student s
                WHERE s.active = false
                  AND (
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  )
            """)
    List<StudentDeactivation> searchDeactivatedStudents(@Param("keyword") String keyword);

}