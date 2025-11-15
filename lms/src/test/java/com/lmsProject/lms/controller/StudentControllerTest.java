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

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    StudentController controller;

    @Test
    void viewStudentDashboard_returnsViewAndModel() {
        Long studentId = 1L;
        Student student = new Student();
        when(studentService.getStudent(studentId)).thenReturn(student);

        Model model = new ExtendedModelMap();
        String view = controller.viewStudentDashboard(studentId, model);
        assertEquals("student-dashboard", view);
        assertEquals(student, model.getAttribute("student"));
    }

    @Test
    void addStudent_redirectsToList() {
        Student student = new Student();
        String resp = controller.addStudent(student);
        assertEquals("redirect:/student/view", resp);
    }

    @Test
    void getAllStudents_returnsList() {
        when(studentService.getAllStudents()).thenReturn(List.of(new Student()));
        ResponseEntity<List<Student>> resp = controller.getAllStudents();
        assertEquals(200, resp.getStatusCodeValue());
    }
}
