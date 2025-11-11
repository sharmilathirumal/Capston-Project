package com.lmsProject.lms.entity;

import java.util.List;

import com.lmsProject.lms.enums.InstructorStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String phone;

    private String specialization;

    private String bio;

    private boolean active = true;

     @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    /*  @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> courses;*/
}
