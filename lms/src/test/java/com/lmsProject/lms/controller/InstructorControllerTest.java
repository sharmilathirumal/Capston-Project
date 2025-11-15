package com.lmsProject.lms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.service.InstructorService;

@ExtendWith(MockitoExtension.class)
public class InstructorControllerTest {

    @Mock
    InstructorService instructorService;

    @InjectMocks
    InstructorController controller;

    @Test
    void mentorDashboard_returnsViewWithMentor() {
        Long mentorId = 1L;
        Instructor inst = new Instructor();
        when(instructorService.getInstructorById(mentorId)).thenReturn(inst);

        Model model = new ExtendedModelMap();
        String view = controller.mentorDashboard(mentorId, model);

        assertEquals("mentor-dashboard", view);
        assertEquals(inst, model.getAttribute("mentor"));
    }

    @Test
    void createInstructor_returnsCreatedResponse() {
        Instructor instructor = new Instructor();
        Instructor created = new Instructor();
        when(instructorService.addInstructor(instructor)).thenReturn(created);

        ResponseEntity<?> resp = controller.createInstructor(instructor);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(created, resp.getBody());
    }

    @Test
    void getAllInstructors_returnsOkWithList() {
        when(instructorService.getAllInstructors()).thenReturn(List.of(new Instructor()));
        ResponseEntity<?> resp = controller.getAllInstructors();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }
}
