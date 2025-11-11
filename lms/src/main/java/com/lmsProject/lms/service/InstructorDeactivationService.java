package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.InstructorDeactivation;

public interface InstructorDeactivationService {
    List<InstructorDeactivation> getAllDeactivatedInstructor();
    List<InstructorDeactivation> searchDeactivatedInstructors(String keyword);
    InstructorDeactivation addInactiveInstructor(Long id,InstructorDeactivation instructor);
    //InstructorDeactivation reactivateInstructor(Long id,String performedBy);
}
