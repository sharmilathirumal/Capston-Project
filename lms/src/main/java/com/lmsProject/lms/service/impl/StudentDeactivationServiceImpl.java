package com.lmsProject.lms.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.entity.StudentDeactivation;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.repository.StudentDeactivationRepository;
import com.lmsProject.lms.repository.StudentRepository;
import com.lmsProject.lms.service.StudentDeactivationService;
import com.lmsProject.lms.service.UserService;

@Service
public class StudentDeactivationServiceImpl implements StudentDeactivationService {

    @Autowired
    private StudentDeactivationRepository studentDeactivationRepository;
    @Autowired
    private StudentRepository studentRepositoy;

    @Autowired
    private UserService userService;

    @Override
    public StudentDeactivation deactivateStudent(Long studentId, StudentDeactivation deactivatedStudent) {
        Student student = studentRepositoy.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setActive(false);
        studentRepositoy.save(student);
        User user = userService.getUserByUsername(student.getName());
        user.setActive(false);
        StudentDeactivation studentDeactivation = new StudentDeactivation();
        studentDeactivation.setStudent(student);
        studentDeactivation.setReason(deactivatedStudent.getReason());
        studentDeactivation.setCount(1);
        studentDeactivation.setPerformedBy(deactivatedStudent.getPerformedBy());
        return studentDeactivationRepository.save(studentDeactivation);
    }

    @Override
    public List<StudentDeactivation> getAllDeactivations() {
        return studentDeactivationRepository.findAllDeactivatedStudents();
    }

    @Override
    public StudentDeactivation getDeactivationById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDeactivationById'");
    }

    @Override
    public List<StudentDeactivation> getDeactivationsByStudent(Long studentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDeactivationsByStudent'");
    }

    @Override
    public List<StudentDeactivation> getRecentDeactivations(int days) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentDeactivations'");
    }

    @Override
    public String getDeactivationReasonsByStudent(Long studentId) {
        Student student = studentRepositoy.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        StudentDeactivation studentDeactivation = studentDeactivationRepository.findByStudent(student);
        return studentDeactivation.getReason();
    }

    @Override
    public StudentDeactivation reactivateStudent(Long deactivationId, String performedBy) {
        StudentDeactivation studentDeactivation = studentDeactivationRepository.findById(deactivationId).orElseThrow(()->new RuntimeException("Deactivation not found"));
        Student student = studentRepositoy.findById(studentDeactivation.getStudent().getId()).orElseThrow(()->new RuntimeException("Student not found"));
        student.setActive(true);
        studentRepositoy.save(student);
        User user = userService.getUserByUsername(student.getName());
        user.setActive(true);
        userService.addUser(user);
        studentDeactivation.setCount(studentDeactivation.getCount() + 1);
        studentDeactivation.setReactivated(true);
        studentDeactivation.setDeactivatedAt(LocalDateTime.now());
        studentDeactivation.setReactivatedBy("Thirumal");
        studentDeactivationRepository.save(studentDeactivation);
        return studentDeactivation;
    }

    @Override
    public List<StudentDeactivation> searchDeactivatedStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentDeactivationRepository.findAllDeactivatedStudents();
        }
        return studentDeactivationRepository.searchDeactivatedStudents(keyword);
    }

}
