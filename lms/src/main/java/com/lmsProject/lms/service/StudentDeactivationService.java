package com.lmsProject.lms.service;

import java.util.List;
import com.lmsProject.lms.entity.StudentDeactivation;

public interface StudentDeactivationService {

    StudentDeactivation deactivateStudent(Long studentId,StudentDeactivation studentDeactivation);

    List<StudentDeactivation> getAllDeactivations();

    StudentDeactivation getDeactivationById(Long id);

    List<StudentDeactivation> getDeactivationsByStudent(Long studentId);

    String getDeactivationReasonsByStudent(Long studentId);
    //void deleteDeactivation(Long id);

    List<StudentDeactivation> getRecentDeactivations(int days);

    StudentDeactivation reactivateStudent(Long deactivationId,String reactivatedBy);

    List<StudentDeactivation> searchDeactivatedStudents(String keyword);

}
