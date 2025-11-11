package com.lmsProject.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
     boolean existsByEmail(String email);

     @Query("SELECT Count(*) From Student s where s.email=:email")
     int getCountOfEmail(String email);

     Student findByEmail(String email);

     List<Student> findByActive(boolean active);

     List<Student> findByActiveAndNameContainingIgnoreCaseOrEmailContainingIgnoreCase(boolean activeStudent,
               String name, String email);

     @Query("""
                   SELECT s
                   FROM Student s
                   WHERE s.active = true
                   AND s.id NOT IN (
                       SELECT e.student.id
                       FROM Enrollment e
                       WHERE e.course.id = :courseId
                       AND e.isActive = true
                   )
               """)
     List<Student> findActiveStudentsNotEnrolledInCourse(@Param("courseId") Long courseId);

     @Query("SELECT s from Student s WHERE LOWER(s.name) = LOWER(:name)")
     Optional<Student> findByName(String name);

}
