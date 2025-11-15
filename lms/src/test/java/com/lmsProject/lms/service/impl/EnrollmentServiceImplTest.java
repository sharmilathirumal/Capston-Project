package com.lmsProject.lms.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Enrollment;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.repository.CourseRepository;
import com.lmsProject.lms.repository.EnrollmentRepository;
import com.lmsProject.lms.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    EnrollmentServiceImpl service;

    @Test
    void enrollStudentInCourse_success() {
        Student s = new Student(); s.setId(1L);
        Course c = new Course(); c.setId(2L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(s));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(c));
        when(enrollmentRepository.existsByStudentAndCourse(s, c)).thenReturn(false);
        Enrollment saved = new Enrollment();
        when(enrollmentRepository.save(org.mockito.Mockito.any())).thenReturn(saved);

        Enrollment result = service.enrollStudentInCourse(1L, 2L);
        assertEquals(saved, result);
        verify(enrollmentRepository).save(org.mockito.Mockito.any(Enrollment.class));
    }

    @Test
    void enrollStudentInCourse_alreadyEnrolled_throws() {
        Student s = new Student(); s.setId(1L);
        Course c = new Course(); c.setId(2L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(s));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(c));
        when(enrollmentRepository.existsByStudentAndCourse(s, c)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.enrollStudentInCourse(1L, 2L));
    }
}
