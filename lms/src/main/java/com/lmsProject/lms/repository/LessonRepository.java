package com.lmsProject.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson,Long>{

    @Query("SELECT l FROM Lesson l WHERE LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
        + "LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Lesson> searchLessons(@Param(value = "keyword") String keyword);

    List<Lesson> findByCourseIdOrderByOrderNumberAsc(Long courseId);
}