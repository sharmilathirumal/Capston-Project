package com.lmsProject.lms.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lmsProject.lms.entity.CourseDeactivation;
import com.lmsProject.lms.entity.StudentDeactivation;
import com.lmsProject.lms.service.CourseDeactivationService;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/inactive/course")
public class CourseDeactivationController {
    @Autowired
    public CourseDeactivationService courseDeactivationService;

    @Autowired
    public com.lmsProject.lms.service.CourseService courseService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add/{id}")
    public String showForm(@PathVariable Long id,Model model){
        model.addAttribute("course",courseService.getCourseById(id));
        model.addAttribute("performedBy", new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser().getUsername());
        return "deactivate-course";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add/{id}")
    public ResponseEntity<?> deactivateCourse(
            @PathVariable Long id,
            @RequestBody CourseDeactivation courseDeactivation) {

        courseDeactivationService.deactivateCourse(id, courseDeactivation);

        return ResponseEntity.ok(Map.of("success", true, "message", "Course deactivated successfully"));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get")
    public String getAllDeactivatedCourses(@RequestParam(required = false) String keyword, Model model){
        List<CourseDeactivation> deactivatedCourses;
        if (keyword != null && !keyword.isEmpty()) {
            deactivatedCourses = courseDeactivationService.searchDeactivatedCourses(keyword);
        } else {
            deactivatedCourses = courseDeactivationService.getAllDeactivatedCourses();
        }
        model.addAttribute("courses", deactivatedCourses);
        return "inactive-courses";
    }
}
