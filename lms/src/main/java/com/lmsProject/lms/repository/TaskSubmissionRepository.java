package com.lmsProject.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.MediaFile;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.entity.TaskSubmission;

public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Long> {

  List<TaskSubmission> findByStudentId(Long studentId);

  List<TaskSubmission> findByInstructorId(Long instructorId);

  @Query("""
          SELECT ts
          FROM TaskSubmission ts
          JOIN ts.task t
          JOIN t.lesson l
          JOIN l.course c
          JOIN c.instructor i
          WHERE i.id = :instructorId
            AND ts.marks IS NULL
      """)
  List<TaskSubmission> getPendingSubmissionsForInstructor(@Param("instructorId") Long instructorId);

  @Query("""
          SELECT c.instructor.id
          FROM MediaFile t
          JOIN t.lesson l
          JOIN l.course c
          WHERE t.id = :submissionId
      """)
  Long findInstructorIdBySubmissionId(@Param("submissionId") Long submissionId);

  @Query("SELECT ts FROM TaskSubmission ts " +
      "JOIN FETCH ts.task m " +
      "JOIN FETCH m.lesson l " +
      "WHERE ts.id = :id")
  TaskSubmission findWithLesson(@Param("id") Long id);

  boolean existsByTaskAndStudent(MediaFile task, Student student);

@Query("SELECT ts FROM TaskSubmission ts WHERE ts.instructor.id=:instructorId AND ts.marks IS NULL")
  List<TaskSubmission> findPendingReviews(@Param("instructorId") Long instructorId);

  Optional<TaskSubmission> findByTaskId(Long id);

}