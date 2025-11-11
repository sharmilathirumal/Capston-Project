package com.lmsProject.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.enums.InstructorStatus;
import com.lmsProject.lms.service.InstructorService;

@Controller
@CrossOrigin(originPatterns = "*")
@RequestMapping("/instructor")
public class InstructorController {
    // implement controller methods according to the service methods
    @Autowired
    private InstructorService instructorService;

    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    @GetMapping("/dashboard/{mentorId}")
    public String mentorDashboard(@PathVariable Long mentorId, Model model) {
        model.addAttribute("mentor", instructorService.getInstructorById(mentorId));
        return "mentor-dashboard";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/view")
    public String showInstructorsPage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Instructor> instructors;
        if (keyword != null && !keyword.isEmpty()) {
            instructors = instructorService.searchInstructors(true, keyword);
        } else {
            instructors = instructorService.getInstructorsByStatus(true);
        }
        // System.out.println(instructors.get(0));
        // List<Student> students = studentService.searchStudents(keyword);
        model.addAttribute("instructors", instructors);
        model.addAttribute("keyword", keyword);
        return "instructor-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add")
    public String addInstructor(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "add-instructor";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> createInstructor(@RequestBody Instructor instructor) {
        Instructor createdInstructor = instructorService.addInstructor(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstructor);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/get/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.getInstructorById(id);
        return ResponseEntity.ok(instructor);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("instructor", instructorService.getInstructorById(id));
        return "update-instructor";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<Instructor> updateInstructor(@ModelAttribute Instructor instructor) {
        Instructor updatedInstructor = instructorService.updateDetails(instructor.getId(), instructor);
        return ResponseEntity.ok(updatedInstructor);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam InstructorStatus status) {
        // instructorService.updateInstructorStatus(id, status);
        return ResponseEntity.ok("status updated successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getInstructorsByStatus(@PathVariable boolean status) {
        return ResponseEntity.ok(instructorService.getInstructorsByStatus(status));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Instructor> getInstructorByEmail(@PathVariable String email) {
        Instructor instructor = instructorService.getInstructorByEmail(email);
        return ResponseEntity.ok(instructor);
    }

    /*
     * @GetMapping("/search")
     * public ResponseEntity<?> searchInstructors(@RequestParam String keyword) {
     * return ResponseEntity.ok(instructorService.searchInstructors(keyword));
     * }
     */
}
