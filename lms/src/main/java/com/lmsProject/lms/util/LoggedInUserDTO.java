package com.lmsProject.lms.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggedInUserDTO {
    private Long id;
    private String username;
    private String role;
}
