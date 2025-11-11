package com.lmsProject.lms.controller;

import com.lmsProject.lms.entity.Lesson;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.service.CourseService;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.LessonService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/view/{courseId}")
    public String viewAllLessons(Model model, @PathVariable Long courseId, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Lesson> lessons;
        if (keyword != null && !keyword.isEmpty()) {
            lessons = lessonService.searchLessons(keyword);
        } else {
            lessons = lessonService.getLessonsByCourseId(courseId);
        }
        model.addAttribute("lessons", lessons);
        model.addAttribute("courseId", courseId);
        return "all-lesson-view";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/add/{courseId}")
    public String viewAddForm(Model model, @PathVariable Long courseId) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("courseId", courseId);
        return "add-lesson";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @PostMapping("/add/{courseId}")
    public ResponseEntity<?> createLesson(@PathVariable Long courseId, @ModelAttribute Lesson lesson) {
        lessonService.createLesson(lesson, courseId);
        return ResponseEntity.ok().body("Lesson Added Successfully!");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/edit/{id}")
    public String viewEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("lesson", lessonService.getLessonById(id));
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        return "update-lesson";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @PostMapping("/update")
    public ResponseEntity<?> updateLesson(@ModelAttribute Lesson lesson) {
        lessonService.updateLesson(lesson.getId(), lesson);
        return ResponseEntity.ok("Lesson updated successfully");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/get/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok("Lesson deleted successfully");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("/getByCourse/{courseId}")
    public String getLessonsByCourseId(@PathVariable Long courseId, Model model) {
        model.addAttribute("lessons", lessonService.getLessonsByCourseId(courseId));
        model.addAttribute("courseId", courseId);
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        return "all-lessons-by-course";
    }

}
