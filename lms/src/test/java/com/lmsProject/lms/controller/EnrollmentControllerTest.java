package com.lmsProject.lms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Enrollment;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.service.CourseService;
import com.lmsProject.lms.service.EnrollmentService;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class EnrollmentControllerTest {

    @Mock
    EnrollmentService enrollmentService;

    @Mock
    CourseService courseService;

    @Mock
    StudentService studentService;

    @Mock
    InstructorService instructorService;

    @InjectMocks
    EnrollmentController controller;

    @Test
    void showEnrollForm_populatesModelAndReturnsView() {
        Long courseId = 1L;
        Course course = new Course();
        when(courseService.getCourseById(courseId)).thenReturn(course);
        when(studentService.findActiveStudentsNotEnrolledInCourse(courseId)).thenReturn(List.of(new Student()));

        Model model = new ExtendedModelMap();
        String view = controller.showEnrollForm(model, courseId);

        assertEquals("enroll-student", view);
        assertEquals(course, model.getAttribute("course"));
    }

    @Test
    void enrollStudent_callsServiceAndReturnsOk() {
        Long courseId = 1L;
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(new Student());
        ResponseEntity<?> resp = controller.enrollStudent(courseId, enrollment);
        assertEquals(200, resp.getStatusCodeValue());
        verify(enrollmentService).enrollStudentInCourse(enrollment.getStudent().getId(), courseId);
    }

}
