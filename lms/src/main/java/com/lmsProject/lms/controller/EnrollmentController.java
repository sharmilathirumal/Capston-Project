package com.lmsProject.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Enrollment;
import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.service.CourseService;
import com.lmsProject.lms.service.EnrollmentService;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

@Controller
@CrossOrigin("*")
@RequestMapping("/enroll")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add/{courseId}")
    public String showEnrollForm(Model model, @PathVariable Long courseId) {
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("students", studentService.findActiveStudentsNotEnrolledInCourse(courseId));
        model.addAttribute("enrollment", new Enrollment());
        return "enroll-student";
    }

    @PostMapping("/add/{courseId}")
    public ResponseEntity<?> enrollStudent(@PathVariable Long courseId, @ModelAttribute Enrollment enrollment) {
        enrollmentService.enrollStudentInCourse(enrollment.getStudent().getId(), courseId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deactivate/{id}")
    public String deactivateEnrollment(@PathVariable Long id) {
        enrollmentService.deactivateEnrollment(id);
        return "Enrollment deactivated successfully.";
    }

    @PutMapping("/status/{id}")
    public Enrollment updateEnrollmentStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        return enrollmentService.updateEnrollmentStatus(id, isActive);
    }

    @GetMapping("/get")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/get/active")
    public List<Enrollment> getActiveEnrollments() {
        return enrollmentService.getActiveEnrollments();
    }

    @GetMapping("/get/{id}")
    public Enrollment getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return "Enrollment deleted successfully.";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/students/{courseId}")
    public String viewEnrolledStudents(@PathVariable Long courseId, Model model) {
        Course course = courseService.getCourseById(courseId);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);

        return "enrolled-students-list";
    }
}
