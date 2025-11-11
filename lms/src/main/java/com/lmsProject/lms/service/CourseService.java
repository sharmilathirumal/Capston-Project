package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.Course;

public interface CourseService {

    Course addCourse(Course course);
    Course updateCourse(Long id,Course course);
    List<Course> getAllCourses();
    Course getCourseById(Long id);
    void deleteCourse(Long id);
    void assignCourses(Long instructorId,Long courseId);
    List<Course> getAllCoursesByActiveState(boolean isActive);
    List<Course> searchCourses(boolean active,String keyword);
    List<Course> getCoursesByInstructorId(Long instructorId);
}