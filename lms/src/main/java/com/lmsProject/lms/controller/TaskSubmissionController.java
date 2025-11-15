package com.lmsProject.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.TaskSubmission;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.MediaFileService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.service.TaskSubmissionService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

import lombok.extern.java.Log;

import java.util.List;

@Controller
@RequestMapping("/taskSubmission")
@CrossOrigin("*")
public class TaskSubmissionController {

    @Autowired
    private TaskSubmissionService taskSubmissionService;

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MediaFileService mediaFileService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("/view/{taskId}")
    public String showTaskSubmissionFormString(@PathVariable Long taskId, Model model) {
       Long lessonId = mediaFileService.getMediaFileById(taskId).getLesson().getId();
        model.addAttribute("taskSubmission", new TaskSubmission());
        model.addAttribute("taskId", taskId);
        model.addAttribute("studentId", 1L);
        model.addAttribute("lessonId", lessonId);
        return "submit-task";
    }
    /*@GetMapping("/submit/{taskId}/{studentId}")
    public String showSubmissionForm(@PathVariable Long taskId,
                                     @PathVariable Long studentId,
                                     Model model) {
        model.addAttribute("taskSubmission", new TaskSubmission());
        model.addAttribute("taskId", taskId);
        model.addAttribute("studentId", studentId);
        return "submit-task";
    }*/

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @GetMapping("/submit/{taskId}/{studentId}")
    public String showSubmissionForm(@PathVariable Long taskId,
                                     @PathVariable Long studentId,
                                     Model model) {
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        model.addAttribute("taskSubmission", new TaskSubmission());
        model.addAttribute("taskId", taskId);
        model.addAttribute("studentId", loggedInUser.getId());
        return "submit-task";
    }

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @PostMapping("/submit/{taskId}/{studentId}")
    public String submitTask(@ModelAttribute TaskSubmission taskSubmission,
                             @PathVariable Long taskId,
                             @PathVariable Long studentId) {
        taskSubmissionService.submitTask(taskSubmission, studentId, taskId);
        return "tasks-view";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("/viewByTask/{taskId}")
    public String viewSubmissionsByTask(@PathVariable Long taskId, Model model) {
        List<TaskSubmission> submissions = taskSubmissionService.getSubmissionsByTask(taskId);
        model.addAttribute("submissions", submissions);
        return "task-submission-list";
    }

    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    @GetMapping("/review/{submissionId}")
    public String reviewTaskSubmission(@PathVariable Long submissionId, Model model) {
        LoggedInUserDTO loggedInUserDTO = new AuthenticatedUserUtil(instructorService,studentService).getLoggedInUser();   
        TaskSubmission submission = taskSubmissionService.getSubmissionById(submissionId);
        model.addAttribute("instructorId", instructorService.getByUserName(loggedInUserDTO.getUsername()).getId());
        model.addAttribute("submission", submission);
        return "review-task";
    }

    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    @PostMapping("/grade/{submissionId}")
    public ResponseEntity<?> gradeSubmission(@PathVariable Long submissionId,
                                  @RequestParam Integer marks,
                                  @RequestParam String feedback) {
        taskSubmissionService.gradeSubmission(submissionId, marks, feedback);
        return ResponseEntity.ok().body("Task Reviewed Successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    @GetMapping("/pending/{instructorId}")
    public String viewPendingSubmissionsForInstructor(@PathVariable Long instructorId, Model model) {
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        if(loggedInUser.getRole().equals("INSTRUCTOR")) {
            model.addAttribute("user", instructorService.getInstructorById(loggedInUser.getId()));
        }
        List<TaskSubmission> pendingSubmissions = taskSubmissionService.getPendingSubmissionsForInstructor(instructorId);
        model.addAttribute("pendingSubmissions", pendingSubmissions);
        return "pending-tasks";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("/viewMarks/{taskId}")
    public String viewMarks(@PathVariable Long taskId, Model model) {
        TaskSubmission submission = taskSubmissionService.getByTaskId(taskId);
        model.addAttribute("submission", submission);
        //model.addAttribute("lessonId", taskSubmissionService.getSubmissionWithLesson(taskId).getTask().getLesson().getId());
       // System.out.println("lessonId: " + taskSubmissionService.getSubmissionWithLesson(taskId).getTask().getLesson().getId());
        return "view-marks";
    }
}
