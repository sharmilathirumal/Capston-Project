package com.lmsProject.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashGeneratorController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/generate-hash")
    public String generateHash() {
        // Generate hash for Admin
        String adminPassword = "Admin@123";
        String adminHash = passwordEncoder.encode(adminPassword);
        
        // Generate hash for Raja
        String rajaPassword = "Raja@123";
        String rajaHash = passwordEncoder.encode(rajaPassword);
        
        // Generate hash for Rene
        String renePassword = "Rene@123";
        String reneHash = passwordEncoder.encode(renePassword);
        
        return String.format("""
            1. For Admin:
            Password: %s
            Generated hash: %s
            
            SQL to update Admin:
            UPDATE user SET password='%s' WHERE username='Admin';
            
            2. For Instructor Raja:
            Password: %s
            Generated hash: %s
            
            SQL to update Raja:
            UPDATE user SET password='%s' WHERE username='Raja';
            
            3. For Student Rene:
            Password: %s
            Generated hash: %s
            
            SQL to update Rene:
            UPDATE user SET password='%s' WHERE username='Rene';
            """, 
            adminPassword, adminHash, adminHash,
            rajaPassword, rajaHash, rajaHash,
            renePassword, reneHash, reneHash);
    }
}