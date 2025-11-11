package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.Student;

public interface StudentService {
    Student addStudent(Student student);

    Student updateStudent(Long id, Student student);

    List<Student> getAllStudents();

    Student getStudent(Long id);

    void deleteStudent(Long id);

    List<Student> searchStudents(boolean active,String keyword);

    List<Student> findByActiveState(boolean active);

    List<Student> findActiveStudentsNotEnrolledInCourse(Long courseId);

    Student getByUserName(String username);

}
