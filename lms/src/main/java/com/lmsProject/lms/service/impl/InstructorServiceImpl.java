package com.lmsProject.lms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.Instructor;
import com.lmsProject.lms.entity.User;
import com.lmsProject.lms.enums.Role;
import com.lmsProject.lms.repository.InstructorRepository;
import com.lmsProject.lms.service.InstructorService;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        if(instructorRepository.getCount(instructor.getEmail())>0){
            throw new RuntimeException("Instructor with this email already exists");
        }
        if(instructorRepository.getCountOfPhoneNumber(instructor.getPhone())>0){
            throw new RuntimeException("Instructor with this phone number already exists");
        }
        User user = new User();
        long id = instructorRepository.count()+1;
        user.setUsername(instructor.getName());
        user.setPassword(passwordEncoder.encode(instructor.getName()+"#"+id+"@!"));
        user.setRole(Role.INSTRUCTOR);
        userService.addUser(user);
        instructor.setUser(user);
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor updateDetails(Long id, Instructor updatedDetails) {
        Instructor existing = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        if (!updatedDetails.getEmail().equalsIgnoreCase(existing.getEmail())) {
            instructorRepository.findByEmail(updatedDetails.getEmail())
                    .ifPresent(other -> {
                        if (!other.getId().equals(id)) {
                            throw new RuntimeException("Email already exists");
                        }
                    });
            existing.setEmail(updatedDetails.getEmail());
        }

        if(!updatedDetails.getPhone().equals(existing.getPhone())){
            instructorRepository.findByPhone(updatedDetails.getPhone()).ifPresent(i->{
                if(!i.getId().equals(id)){
                    throw new RuntimeException("Phone number already exists");
                }
            });
            existing.setPhone(updatedDetails.getPhone());
        }
        existing.setName(updatedDetails.getName());
        existing.setPhone(updatedDetails.getPhone());
        existing.setBio(updatedDetails.getBio());
        existing.setSpecialization(updatedDetails.getSpecialization());

        return instructorRepository.save(existing);
    }

    @Override
    public void updateInstructorStatus(Long id, boolean status) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
        instructor.setActive(status);
        User user = userService.getUserByUsername(instructor.getName());
        user.setActive(status); 
        //userService.updateUser(user);
        instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public List<Instructor> getInstructorsByStatus(boolean status) {
        return instructorRepository.findByActive(status);
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
    }

    @Override
    public Instructor getInstructorByEmail(String email) {
        return instructorRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Instructor with this email not found"));
    }

    @Override
    public List<Instructor> searchInstructors(boolean activeState ,String keyword) {
        return instructorRepository.searchByActiveAndKeyword(activeState,keyword);
    }

    @Override
    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(()->new RuntimeException("Instructor not found"));
        instructorRepository.delete(instructor);
    }

    public Instructor getByUserName(String username){
        Instructor instructor = instructorRepository.findByName(username).orElseThrow(()->new RuntimeException("Instructor not found"));
        return instructor;
    }
}
