package com.lmsProject.lms.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class FileStorageInitializer {
    
    @PostConstruct
    public void init() {
        File uploadDir = new File("uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
}