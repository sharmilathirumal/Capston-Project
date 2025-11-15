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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.entity.StudentDeactivation;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentDeactivationService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/inactive/student")
public class StudentDeactivationController {

    @Autowired
    private StudentDeactivationService studentDeactivationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(studentDeactivationService.getAllDeactivations());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search")
    public String searchDeactivatedStudents(@RequestParam(required = false) String keyword, Model model) {
        List<StudentDeactivation> results = studentDeactivationService.searchDeactivatedStudents(keyword);
        model.addAttribute("deactivatedStudents", results);
        model.addAttribute("keyword", keyword);
        // System.out.println(results);
        return "inactive-student-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/view")
    public String getDeactivatedStudentView(@RequestParam(value = "keyword", required = false) String keyword,
            Model model) {
        List<StudentDeactivation> deactivatedStudents;
        if (keyword != null && !keyword.isEmpty()) {
            deactivatedStudents = studentDeactivationService.searchDeactivatedStudents(keyword);
        } else {
            deactivatedStudents = studentDeactivationService.getAllDeactivations();
        }
        Map<Long, Long> deactivationCounts = deactivatedStudents.stream()
    .collect(Collectors.groupingBy(d -> d.getStudent().getId(), Collectors.counting()));

        model.addAttribute("deactivatedStudents", deactivatedStudents);
        model.addAttribute("deactivationCounts", deactivationCounts);
        model.addAttribute("keyword", keyword);
        return "inactive-student-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add/{studentId}")
    public String showDeactivateForm(@PathVariable Long studentId, Model model) {
        Student student = studentService.getStudent(studentId);
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
       model.addAttribute("performedBy", loggedInUser.getUsername());
        model.addAttribute("student", student);
        return "deactivate-student";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add/{studentId}")
    public ResponseEntity<?> deactivateStudent(@PathVariable Long studentId,
            @ModelAttribute StudentDeactivation studentDeactivation) {
        return ResponseEntity.ok().body(studentDeactivationService.deactivateStudent(studentId, studentDeactivation));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/reactivate/{deactivationId}")
    public String reactivateStudent(@PathVariable Long deactivationId,String reactivatedBy){
            studentDeactivationService.reactivateStudent(deactivationId, reactivatedBy);
            return "redirect:/student/view";
    }
    /*
     * @PostMapping("/add/{studentId}")
     * public ResponseEntity<?> addStudent(@PathVariable Long studentId,@RequestBody
     * StudentDeactivation studentDeactivation){
     * return ResponseEntity.ok().body(studentDeactivationService.deactivateStudent(
     * studentId,studentDeactivation));
     * }
     */
}
