package com.lmsProject.lms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.service.CourseService;
import com.lmsProject.lms.service.EnrollmentService;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    CourseService courseService;

    @Mock
    InstructorService instructorService;

    @Mock
    EnrollmentService enrollmentService;

    @Mock
    StudentService studentService;

    @InjectMocks
    CourseController controller;

    @Test
    void viewCourses_returnsViewAndModel() {
        when(courseService.getAllCoursesByActiveState(true)).thenReturn(List.of(new Course()));
        Model model = new ExtendedModelMap();
        String view = controller.viewCourses(null, model);
        assertEquals("all-course-view", view);
    }

    @Test
    void addCourse_returnsOk() {
        Course course = new Course();
        when(courseService.addCourse(course)).thenReturn(course);
        ResponseEntity<?> resp = controller.addCourse(course);
        assertEquals(200, resp.getStatusCodeValue());
    }

    @Test
    void getAllCourse_returnsOk() {
        when(courseService.getAllCourses()).thenReturn(List.of(new Course()));
        ResponseEntity<?> resp = controller.getAllCourse();
        assertEquals(200, resp.getStatusCodeValue());
    }
}
