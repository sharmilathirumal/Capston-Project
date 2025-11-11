package com.lmsProject.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Student;

public interface CourseRepository extends JpaRepository<Course,Long>{
    List<Course> findAllByActive(boolean active);
    List<Course> findByActiveAndTitleContainingIgnoreCase(boolean active, String title);
    @Query("SELECT e.student FROM Enrollment e WHERE e.course.id = :courseId AND e.isActive = true")
    List<Student> findActiveStudentsByCourseId(@Param("courseId") Long courseId);
    List<Course> findByInstructorId(Long instructorId);
}
