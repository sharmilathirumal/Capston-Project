package com.lmsProject.lms.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.repository.InstructorRepository;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceImplTest {

    @Mock
    InstructorRepository instructorRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    InstructorServiceImpl service;

    @Test
    void addInstructor_success() {
        Instructor instructor = new Instructor();
        instructor.setEmail("a@b.com");
        instructor.setPhone("123");
        when(instructorRepository.getCount(instructor.getEmail())).thenReturn(0);
        when(instructorRepository.getCountOfPhoneNumber(instructor.getPhone())).thenReturn(0);
        when(passwordEncoder.encode(org.mockito.Mockito.anyString())).thenReturn("encoded");
        when(instructorRepository.save(org.mockito.Mockito.any())).thenAnswer(a -> a.getArgument(0));

        Instructor result = service.addInstructor(instructor);
        assertEquals(instructor.getEmail(), result.getEmail());
    }

    @Test
    void addInstructor_duplicateEmail_throws() {
        Instructor instructor = new Instructor();
        instructor.setEmail("a@b.com");
        when(instructorRepository.getCount(instructor.getEmail())).thenReturn(1);
        assertThrows(RuntimeException.class, () -> service.addInstructor(instructor));
    }
}
