package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Enrollment;
import com.lmsProject.lms.entity.Student;

public interface EnrollmentService {

    Enrollment enrollStudentInCourse(Long studentId, Long courseId);

    void deactivateEnrollment(Long enrollmentId);

    Enrollment getEnrollmentById(Long enrollmentId);

    List<Enrollment> getAllEnrollments();

    void deleteEnrollment(Long enrollmentId);//rarely delete

    List<Enrollment> getEnrollmentsByStudent(Long studentId);

    List<Enrollment> getEnrollmentsByCourse(Long courseId);

    List<Enrollment> getActiveEnrollments();

    Enrollment updateEnrollmentStatus(Long id, boolean isActive);

    List<Student> getStudentsByCourse(Long courseId);

    List<Course> getCoursesByStudent(Long studentId);
}