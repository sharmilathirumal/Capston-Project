package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.InstructorDeactivation;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.repository.InstructorDeactivationRepository;
import com.lmsProject.lms.repository.InstructorRepository;
import com.lmsProject.lms.service.InstructorDeactivationService;
import com.lmsProject.lms.service.UserService;

@Service
public class InstructorDeactivationServiceImpl implements InstructorDeactivationService{

    @Autowired
    private InstructorDeactivationRepository repository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<InstructorDeactivation> getAllDeactivatedInstructor() {
        return repository.findAll();
    }

    @Override
    public List<InstructorDeactivation> searchDeactivatedInstructors(String keyword) {
         if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.searchDeactivatedInstructor(keyword);
    }

    @Override
    public InstructorDeactivation addInactiveInstructor(Long instructorId,InstructorDeactivation deactivatedInstructor) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(()->new RuntimeException("Instructor not found"));
        instructor.setActive(false);
        User user = userService.getUserByUsername(instructor.getName());
        user.setActive(false);
        InstructorDeactivation instructorDeactivation = new InstructorDeactivation();
        instructorDeactivation.setPerformedBy(deactivatedInstructor.getPerformedBy());
        instructorDeactivation.setInstructor(instructor);
        instructorDeactivation.setReason(deactivatedInstructor.getReason());
        instructorRepository.save(instructor);
       return repository.save(instructorDeactivation);
    }

  /* @Override
    public InstructorDeactivation reactivateInstructor(Long id, String performedBy) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reactivateInstructor'");
    }*/  
    
}
