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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.service.StudentService;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @GetMapping("/dashboard/{studentId}")
    public String viewStudentDashboard(@PathVariable Long studentId, Model model) {
        Student student = studentService.getStudent(studentId);
        model.addAttribute("student", student);
        return "student-dashboard";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/view")
    public String viewStudents(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Student> students;
        if (keyword != null && !keyword.isEmpty()) {
            students = studentService.searchStudents(true, keyword);
        } else {
            students = studentService.findByActiveState(true);
        }
        // List<Student> students = studentService.searchStudents(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "student-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add")
    public String showAddStudentForm() {
        return "add-student";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student) {
        studentService.addStudent(student);
        return "redirect:/student/view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudent(id));
        return "update-student";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<?> updateStudent(@ModelAttribute Student student) {
        return ResponseEntity.ok(studentService.updateStudent(student.getId(), student));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping("/get")
    @ResponseBody
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/active-not-enrolled/{courseId}")
    public ResponseEntity<List<Student>> findActiveStudentsNotEnrolledInCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.findActiveStudentsNotEnrolledInCourse(courseId));
    }

}
