package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.repository.CourseRepository;
import com.lmsProject.lms.repository.InstructorRepository;
import com.lmsProject.lms.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        existingCourse.setDescription(course.getDescription());
        existingCourse.setTitle(course.getTitle());
        if (course.getInstructor().getId() != existingCourse.getInstructor().getId()) {
            Instructor instructor = instructorRepository.findById(course.getInstructor().getId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
                    existingCourse.setInstructor(instructor);

        }
        // existingCourse.setLessons(course.getLessons());
        return courseRepository.save(existingCourse);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public void assignCourses(Long instructorId, Long courseId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // course.setInstructor(instructor);

        courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCoursesByActiveState(boolean isActive) {
        return courseRepository.findAllByActive(isActive);
    }

    @Override
    public List<Course> searchCourses(boolean active, String keyword) {
        return courseRepository.findByActiveAndTitleContainingIgnoreCase(active, keyword);
    }

    @Override
    public List<Course> getCoursesByInstructorId(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }
}
