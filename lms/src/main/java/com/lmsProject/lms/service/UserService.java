package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.User;

public interface UserService {
    User addUser(User user);
    
    User updatePassword(Long id,String currentPassword ,String newPassword);

    void deleteUser(Long id);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByUsername(String username);

    void setActive(Long id, boolean active);

    void updateUser(String username, User user);

    void RegisterAdmin(String username,String Password,String confirmPassSword);
}
