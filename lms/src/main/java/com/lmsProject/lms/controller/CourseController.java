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
import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.service.CourseService;
import com.lmsProject.lms.service.EnrollmentService;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService ;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/view")
    public String viewCourses(@RequestParam(value = "keyword",required = false) String keyword,Model model){
        List<Course> courses;
        if(keyword !=null && !keyword.isEmpty()){
            courses = courseService.searchCourses(true, keyword);
        }else{
            courses =courseService.getAllCoursesByActiveState(true);
        }
        model.addAttribute("courses",courses);
        return "all-course-view";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/add")
    public String showAddCourseForm(Model model){
        model.addAttribute("course", new Course());
        model.addAttribute("instructors", instructorService.getAllInstructors());
        return "add-course";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@ModelAttribute Course course){
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
     @GetMapping("/edit/{courseId}")
    public String showEditCourseForm(@PathVariable Long courseId,Model model){
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("instructors", instructorService.getAllInstructors());
        return "update-course";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<?> updateCourse(@ModelAttribute Course course){
        return ResponseEntity.ok(courseService.updateCourse(course.getId(), course));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getAllCourse(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/assingCourse")
    public ResponseEntity<?> assignCourse(@RequestParam Long courseId,@RequestParam Long instructorId){
        courseService.assignCourses(instructorId, courseId);
        return ResponseEntity.ok("Course assigned to instuctor successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    @GetMapping("/instructor-courses/{instructorId}")
    public String instructorCourses(@PathVariable Long instructorId, Model model) {
        List<Course> courses = courseService.getCoursesByInstructorId(instructorId);
        model.addAttribute("courses", courses);
        model.addAttribute("instructorId", instructorId);
        return "all-course-by-instructor";
    }

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @GetMapping("/student-courses/{studentId}")
    public String studentCourses(@PathVariable Long studentId, Model model) {
        List<Course> courses = enrollmentService.getCoursesByStudent(studentId);
        model.addAttribute("courses", courses);
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser(); model.addAttribute("loggedInUser", loggedInUser);
        return "all-course-by-student";
    }
}
