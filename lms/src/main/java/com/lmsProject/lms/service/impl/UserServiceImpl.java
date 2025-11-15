package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.enums.Role;
import com.lmsProject.lms.repository.UserRepository;
import com.lmsProject.lms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(Long id, String currentPassword,String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        String existingPassword = user.getPassword();
        if(!passwordEncoder.matches(currentPassword, existingPassword)) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, existingPassword)) {
            throw new RuntimeException("New password must be different from the old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void setActive(Long id, boolean active) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    public void updateUser(String username,User user) {
        User existingUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(existingUser);
    }

    @Override
    public void RegisterAdmin(String username,String Password,String confirmPassSword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if(!Password.equals(confirmPassSword)) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(Password));
        user.setRole(Role.ADMIN);
        user.setActive(true);
        userRepository.save(user);
    }

    
}
