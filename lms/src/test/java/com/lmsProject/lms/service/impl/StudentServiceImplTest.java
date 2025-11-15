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

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.exception.DuplicateEmailException;
import com.lmsProject.lms.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    StudentServiceImpl service;

    @Test
    void addStudent_success() {
        Student s = new Student();
        s.setEmail("a@b.com");
        s.setName("name");
        when(studentRepository.existsByEmail(s.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(org.mockito.Mockito.anyString())).thenReturn("encoded");
        when(studentRepository.save(org.mockito.Mockito.any())).thenAnswer(a -> a.getArgument(0));

        Student result = service.addStudent(s);
        assertEquals(s.getEmail(), result.getEmail());
    }

    @Test
    void addStudent_duplicateEmail_throws() {
        Student s = new Student(); s.setEmail("a@b.com");
        when(studentRepository.existsByEmail(s.getEmail())).thenReturn(true);
        assertThrows(DuplicateEmailException.class, () -> service.addStudent(s));
    }
}
