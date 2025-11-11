package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.CourseDeactivation;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.repository.CourseDeactivationRepository;
import com.lmsProject.lms.repository.CourseRepository;
import com.lmsProject.lms.repository.StudentRepository;
import com.lmsProject.lms.service.CourseDeactivationService;
import com.lmsProject.lms.service.EnrollmentService;

@Service
public class CourseDeactivationServiceImpl implements CourseDeactivationService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseDeactivationRepository deactivationRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepositoy;
    
    @Override
    public void deactivateCourse(Long courseId, CourseDeactivation courseDeactivation) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setActive(false);
        courseRepository.save(course);

        CourseDeactivation deactivation = new CourseDeactivation();
        deactivation.setCourse(course);
        deactivation.setPerformedBy(courseDeactivation.getPerformedBy());
        deactivation.setReason(courseDeactivation.getReason());
        enrollmentService.getEnrollmentsByCourse(courseId).forEach(student->{
            student.setActive(false);
            studentRepositoy.save(student.getStudent());
        });
        deactivationRepository.save(deactivation);
    }

    public List<CourseDeactivation> getDeactivationHistory(Long courseId) {
        return deactivationRepository.findByCourseId(courseId);
    }

    @Override
    public List<CourseDeactivation> getAllCourse() {
        return deactivationRepository.findAll();
    }

    @Override
    public List<CourseDeactivation> searchDeactivatedCourses(String keyword) {
        return deactivationRepository.searchDeactivatedCourses(keyword);
    }

    @Override
    public List<CourseDeactivation> getAllDeactivatedCourses() {
        return deactivationRepository.getAllDeactivationCourses();
    }

}

