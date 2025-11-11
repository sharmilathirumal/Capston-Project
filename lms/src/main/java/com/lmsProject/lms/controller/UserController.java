package com.lmsProject.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.service.UserService;
import com.lmsProject.lms.util.AuthenticatedUserUtil;
import com.lmsProject.lms.util.LoggedInUserDTO;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/view")
    public String showUserDetail(Model model) {
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        if (loggedInUser.getRole().equals("INSTRUCTOR")) {
            model.addAttribute("user", instructorService.getByUserName(loggedInUser.getUsername()));
        } else if (loggedInUser.getRole().equals("STUDENT")) {
            model.addAttribute("user", studentService.getByUserName(loggedInUser.getUsername()));
        }
        model.addAttribute("loggedInUser", loggedInUser);
        return "userdetail-view";
    }

    @GetMapping("/change-password/{id}")
    public String showChangePasswordPage(@PathVariable Long id, Model model) {
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        if (loggedInUser.getRole().equals("INSTRUCTOR")) {
            model.addAttribute("user", instructorService.getByUserName(loggedInUser.getUsername()));
        } else if (loggedInUser.getRole().equals("STUDENT")) {
            model.addAttribute("user", studentService.getByUserName(loggedInUser.getUsername()));
        }
        model.addAttribute("loggedInUser", loggedInUser);
        return "change-password";
    }

    @PostMapping("/update-password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestParam String currentPassword, @RequestParam String newPassword,
            @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
                String username="";
        LoggedInUserDTO loggedInUser = new AuthenticatedUserUtil(instructorService, studentService).getLoggedInUser();
        if(loggedInUser.getRole().equals("INSTRUCTOR")){
            username = instructorService.getByUserName(loggedInUser.getUsername()).getName();
        }else{
            studentService.getByUserName(loggedInUser.getUsername()).getName();
        }
        userService.updatePassword(userService.getUserByUsername(username).getId(), currentPassword, newPassword);
        redirectAttributes.addFlashAttribute("success", "Password updated successfully!");
        return ResponseEntity.ok("Password updated successfully!");
    }

}
