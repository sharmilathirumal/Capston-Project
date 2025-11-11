package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.CourseDeactivation;

public interface CourseDeactivationService {
    void deactivateCourse(Long courseId, CourseDeactivation courseDeactivation);
    List<CourseDeactivation> getAllCourse();
    List<CourseDeactivation> searchDeactivatedCourses(String keyword);
    List<CourseDeactivation> getAllDeactivatedCourses();
}
