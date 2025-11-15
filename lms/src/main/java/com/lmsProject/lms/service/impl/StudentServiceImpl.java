package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.enums.Role;
import com.lmsProject.lms.exception.DuplicateEmailException;
import com.lmsProject.lms.repository.StudentRepository;
import com.lmsProject.lms.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepositoy;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Student addStudent(Student student) {
        if(studentRepositoy.existsByEmail(student.getEmail())){
            throw new DuplicateEmailException("Email already exists!!!");
        }
        long id = studentRepositoy.count()+1;
        User user = new User();
        user.setUsername(student.getName());
        user.setRole(Role.STUDENT);
        user.setPassword(passwordEncoder.encode(student.getName()+"@123"));
        userService.addUser(user);
        student.setUser(user);
        return studentRepositoy.save(student);
    }
    
    @Override
    public Student updateStudent(Long id, Student student) {
        Student existingStudent = studentRepositoy.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        existingStudent.setName(student.getName());
        if(!student.getEmail().equals(existingStudent.getEmail())){
            if(studentRepositoy.getCountOfEmail(student.getEmail())>0){
            if(studentRepositoy.findByEmail(student.getEmail()).getId() !=existingStudent.getId()){
                throw new DuplicateEmailException("Email already exists");
            }
        }
        existingStudent.setEmail(student.getEmail());
        }
        return studentRepositoy.save(existingStudent);
    }

    @Override
    public List<Student> getAllStudents() {
       return studentRepositoy.findAll();
    }

    @Override
    public Student getStudent(Long id) {
       return studentRepositoy.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepositoy.findById(id).orElseThrow(()->new RuntimeException("Student not found"));
        studentRepositoy.delete(student);
    }

    @Override
    public List<Student> searchStudents(boolean active,String keyword) {
        return studentRepositoy.findByActiveAndNameContainingIgnoreCaseOrEmailContainingIgnoreCase(true,keyword, keyword);
    }

    @Override
    public List<Student> findByActiveState(boolean active) {
        return studentRepositoy.findByActive(active);
    }

    @Override
    public List<Student> findActiveStudentsNotEnrolledInCourse(Long courseId) {
        return studentRepositoy.findActiveStudentsNotEnrolledInCourse(courseId);
    }

    @Override
    public Student getByUserName(String username) {
        return studentRepositoy.findByName(username).orElseThrow(()->new RuntimeException("Student not found"));
    }

    
}
