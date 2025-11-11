package com.lmsProject.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDeactivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String performedBy;    

    private String reason;            

    private LocalDateTime deactivatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
