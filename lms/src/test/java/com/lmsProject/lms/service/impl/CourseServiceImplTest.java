package com.lmsProject.lms.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.repository.CourseRepository;
import com.lmsProject.lms.repository.InstructorRepository;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    CourseServiceImpl service;

    @Test
    void addCourse_savesAndReturns() {
        Course c = new Course();
        when(courseRepository.save(c)).thenReturn(c);
        Course result = service.addCourse(c);
        assertEquals(c, result);
    }

    @Test
    void updateCourse_changesInstructorWhenDifferent() {
        Course existing = new Course(); existing.setId(1L);
        Instructor oldInst = new Instructor(); oldInst.setId(10L);
        existing.setInstructor(oldInst);

        Course updated = new Course(); updated.setId(1L);
        Instructor newInst = new Instructor(); newInst.setId(20L);
        updated.setInstructor(newInst);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(instructorRepository.findById(20L)).thenReturn(Optional.of(newInst));
        when(courseRepository.save(existing)).thenReturn(existing);

        Course result = service.updateCourse(1L, updated);
        assertEquals(existing, result);
        assertEquals(newInst.getId(), result.getInstructor().getId());
    }

    @Test
    void updateCourse_missing_throws() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.updateCourse(99L, new Course()));
    }
}
