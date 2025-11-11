package com.lmsProject.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.CourseDeactivation;

public interface CourseDeactivationRepository extends JpaRepository<CourseDeactivation, Long> {
    List<CourseDeactivation> findByCourseId(Long courseId);

    @Query("SELECT cd from CourseDeactivation cd JOIN fetch cd.course c JOIN fetch c.instructor i where c.active=false")
    List<CourseDeactivation> getAllDeactivationCourses();

    @Query("""
            SELECT cd from CourseDeactivation cd JOIN fetch cd.course c
            JOIN fetch c.instructor i where c.active=false
            AND (
            LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    List<CourseDeactivation> searchDeactivatedCourses(@Param("keyword") String keyword);
}
