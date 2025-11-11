package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Enrollment;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.repository.CourseRepository;
import com.lmsProject.lms.repository.EnrollmentRepository;
import com.lmsProject.lms.repository.StudentRepository;
import com.lmsProject.lms.service.EnrollmentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Enrollment enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(()->new RuntimeException("Course not found"));
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentAndCourse(student,course);
        if (alreadyEnrolled) {
            throw new RuntimeException("Student is already enrolled in this course!");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setActive(true);
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void deactivateEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with ID: " + enrollmentId));

        enrollment.setActive(false);
        enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment getEnrollmentById(Long enrollmentId) {
       return enrollmentRepository.findById(enrollmentId).orElseThrow(()->new RuntimeException("Enrollment not found"));
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public void deleteEnrollment(Long enrollmentId) {
     enrollmentRepository.findById(enrollmentId).orElseThrow(()->new RuntimeException("Enrollment not found"));
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudent(student);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()->new RuntimeException("Course not found"));
        return enrollmentRepository.findByCourseId(course.getId());
    }

    @Override
    public List<Enrollment> getActiveEnrollments() {
        return enrollmentRepository.findByIsActive(true);
    }

    @Override
    public Enrollment updateEnrollmentStatus(Long id, boolean isActive) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Enrollment not found"));
        enrollment.setActive(isActive);
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Student> getStudentsByCourse(Long courseId) {
        return enrollmentRepository.findActiveStudentsByCourseId(courseId);
    }
    
    @Override
    public List<Course> getCoursesByStudent(Long studentId) {
        return enrollmentRepository.findCoursesByStudentId(studentId);
    }
}
