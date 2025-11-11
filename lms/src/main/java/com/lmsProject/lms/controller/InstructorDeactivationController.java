package com.lmsProject.lms.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.InstructorDeactivation;
import com.lmsProject.lms.entity.StudentDeactivation;
import com.lmsProject.lms.service.InstructorDeactivationService;
import com.lmsProject.lms.service.InstructorService;

@Controller
@RequestMapping("/instructor/deactivation")
public class InstructorDeactivationController {

    @Autowired
    private  InstructorDeactivationService instructorDeactivationService;

    @Autowired
    private InstructorService instructorService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/view")
    public String viewAllDeactivatedInstructors(@RequestParam(value = "keyword", required = false) String keyword,Model model) {
        List<InstructorDeactivation> deactivatedInstructors;
        if (keyword != null && !keyword.isEmpty()) {
            deactivatedInstructors = instructorDeactivationService.searchDeactivatedInstructors(keyword);
        } else {
            deactivatedInstructors = instructorDeactivationService.getAllDeactivatedInstructor();
        }
        model.addAttribute("deactivatedInstructors", deactivatedInstructors);
        model.addAttribute("keyword", keyword);
        return "inactive-instructor-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search")
    public String searchDeactivatedInstructors(@RequestParam(required = false) String keyword, Model model) {
        List<InstructorDeactivation> results = instructorDeactivationService.searchDeactivatedInstructors(keyword);
        model.addAttribute("deactivatedInstructors", results);
        model.addAttribute("keyword", keyword);
        return "instructor-deactivation-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add/{instructorId}")
    public String showDeactivationForm(@PathVariable Long instructorId, Model model) {
        Instructor instructor = instructorService.getInstructorById(instructorId);
        model.addAttribute("instructor", instructor);
       // model.addAttribute("deactivation", new InstructorDeactivation());
        return "deactivate-instructor";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add/{id}")
    public ResponseEntity<?> deactivateInstructor(@PathVariable Long id,
                                       @RequestBody InstructorDeactivation deactivation) {
        return ResponseEntity.ok().body(instructorDeactivationService.addInactiveInstructor(id, deactivation));
        
    }
}
