package com.lmsProject.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Enrollment;
import com.lmsProject.lms.entity.Student;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentAndCourse(Student student, Course course);

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByCourse(Course course);

    List<Enrollment> findByIsActive(boolean active);

    @Query("""
                SELECT s
                FROM Enrollment e
                JOIN e.student s
                WHERE e.course.id = :courseId
                  AND e.isActive = true
            """)
    List<Student> findActiveStudentsByCourseId(@Param("courseId") Long courseId);

    @Query("""
                SELECT e
                FROM Enrollment e
                JOIN FETCH e.student
                WHERE e.course.id = :courseId AND e.isActive = true
            """)
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT e.course FROM Enrollment e WHERE e.student.id = :studentId AND e.isActive = true")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

}
