package com.lmsProject.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lmsProject.lms.entity.Lesson;
import com.lmsProject.lms.entity.MediaFile;
import com.lmsProject.lms.entity.Student;
import com.lmsProject.lms.enums.FilePurpose;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.LessonService;
import com.lmsProject.lms.service.MediaFileService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.service.TaskSubmissionService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

@Controller
@RequestMapping("/mediaFile")
@CrossOrigin(origins = "*")
public class MediaFileController {
      @Autowired
      private  MediaFileService mediaFileService;

      @Autowired
      private LessonService lessonService;
    
      @Autowired
      private InstructorService instructorService;

      @Autowired
      private StudentService studentService;
      
      @Autowired
      private TaskSubmissionService taskSubmissionService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @GetMapping("/addForm/{lessonId}")
    public String viewAddFileForm(@PathVariable Long lessonId, Model model) {
        model.addAttribute("mediaFile", new MediaFile());
        model.addAttribute("lesson", lessonService.getLessonById(lessonId));
        return "add-mediaFile";
    }  
    
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @PostMapping("/add")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "lessonId") Long lessonId,@RequestParam(value = "filePurpose") String filePurpose) {
        MediaFile uploadedFile = mediaFileService.uploadFile(file, title, description, lessonId, FilePurpose.valueOf(filePurpose));
        return ResponseEntity.ok().body(uploadedFile);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<List<MediaFile>> getAllMediaFiles() {
        List<MediaFile> files = mediaFileService.getAllMediaFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("/get/{id}")
    public ResponseEntity<MediaFile> getMediaFileById(@PathVariable Long id) {
        MediaFile mediaFile = mediaFileService.getMediaFileById(id);
        return new ResponseEntity<>(mediaFile, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMediaFile(@PathVariable Long id) {
        mediaFileService.deleteMediaFile(id);
        return ResponseEntity.ok("File deleted successfully");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("/viewAll/{lessonId}")
    public String viewAllFiles(@PathVariable Long lessonId, Model model) {
        Lesson lesson = lessonService.getLessonById(lessonId);
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("files", mediaFileService.getMediaFilesByPurpose(lessonId, FilePurpose.COURSE_MATERIAL));
        model.addAttribute("lessonId", lesson.getId());
        model.addAttribute("courseId", lesson.getCourse().getId());
        return "material-tasks";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
    @GetMapping("viewByLesson/{lessonId}")
    public String getAllFilesOfLessons(@PathVariable Long lessonId,Model model){
        model.addAttribute("files", mediaFileService.getMediaFilesByPurpose(lessonId, FilePurpose.COURSE_MATERIAL));
        model.addAttribute("lessonId", lessonId);
         LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        return "mediaFile-view";
    }
    
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_INSTRUCTOR','ROLE_STUDENT')")
@GetMapping("/tasks/{lessonId}")
public String getAllTasks(@PathVariable Long lessonId, Model model) {
    // 1️⃣ Get lesson tasks
    List<MediaFile> tasks = mediaFileService.getMediaFilesByPurpose(lessonId, FilePurpose.TASK);
    model.addAttribute("files", tasks);
    model.addAttribute("lessonId", lessonId);

    // 2️⃣ Get logged-in user
    LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
    model.addAttribute("loggedInUser", loggedInUser);

    // 3️⃣ If the user is a student, determine submission status for each task
    Map<Long, Boolean> submissionStatus = new HashMap<>();

    if ("STUDENT".equals(loggedInUser.getRole())) {
        Long studentId = studentService.getByUserName(loggedInUser.getUsername()).getId();

        for (MediaFile task : tasks) {
            boolean isSubmitted = taskSubmissionService.existsByTaskIdAndStudentId(task.getId(), studentId);
            submissionStatus.put(task.getId(), isSubmitted);
        }
    }

    model.addAttribute("submissionStatus", submissionStatus);

    return "tasks-view";
}

}
