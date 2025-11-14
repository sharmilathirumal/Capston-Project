package com.lmsProject.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;
import com.lmsProject.lms.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String redirectBasedOnRole(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/login?error=user_not_found";
        }

        String role = user.getRole().toString();

        if (role == null) {
            return "redirect:/login?error=role_missing";
        }

        switch (role) {
            case "ADMIN":
                return "redirect:/admin/dashboard";

            case "INSTRUCTOR": {
                Long instructorId = instructorService.getByUserName(user.getUsername()).getId();
                return "redirect:/instructor/dashboard/" + instructorId;
            }

            default: {
                Long studentId = studentService.getByUserName(user.getUsername()).getId();
                System.out.println(studentId);
                return "redirect:/student/dashboard/" + studentId;
            }

        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        // model.addAttribute("user", new User()); // Add a new User object to the model
        // for registration form binding
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        try {
            userService.RegisterAdmin(username, password, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Admin registered successfully!");
            return "redirect:/login?registerSuccess";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

   /*  @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password,
            @RequestParam String confirmPassword) {
        try {
            userService.RegisterAdmin(username, password, confirmPassword);
            return "redirect:/login?registerSuccess";
        } catch (RuntimeException e) {
            return "redirect:/register?error=" + e.getMessage();
        }

    }*/

}
