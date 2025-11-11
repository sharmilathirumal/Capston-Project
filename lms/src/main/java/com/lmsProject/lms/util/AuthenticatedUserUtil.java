package com.lmsProject.lms.util;

import com.lmsProject.lms.service.InstructorService;
import com.lmsProject.lms.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserUtil {

    private final InstructorService instructorService;
    private final StudentService studentService;

    public AuthenticatedUserUtil(InstructorService instructorService, StudentService studentService) {
        this.instructorService = instructorService;
        this.studentService = studentService;
    }

    public LoggedInUserDTO getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String username = auth.getName();
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        Long id = null;
        if ("INSTRUCTOR".equals(role)) {
            id = instructorService.getByUserName(username).getId();
        } else if ("STUDENT".equals(role)) {
            id = studentService.getByUserName(username).getId();
        }

        return new LoggedInUserDTO(id, username, role);
    }
}
