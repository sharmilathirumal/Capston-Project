package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.enums.InstructorStatus;

public interface InstructorService {
    Instructor addInstructor(Instructor instructor);
    Instructor updateDetails(Long id,Instructor updatedDetails);
    void updateInstructorStatus(Long id,boolean status);
    List<Instructor> getAllInstructors();
    List<Instructor> getInstructorsByStatus(boolean status);
    Instructor getInstructorById(Long id);
    Instructor getInstructorByEmail(String email);
    List<Instructor> searchInstructors(boolean activeState,String keyword);
    void deleteInstructor(Long id);
    Instructor getByUserName(String name);
    
}
