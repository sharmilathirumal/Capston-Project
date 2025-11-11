package com.lmsProject.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lmsProject.lms.service.InstructorService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    public InstructorService instructorService;

    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    @GetMapping("/dashboard/{mentorId}")
    public String mentorDashboard(@PathVariable Long mentorId, Model model) {
        model.addAttribute("mentor", instructorService.getInstructorById(mentorId));
        return "mentor-dashboard";
    }
}
