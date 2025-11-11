package com.lmsProject.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lmsProject.lms.enums.FilePurpose;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;
    private String fileType;
    private long fileSize;
    @Column(length = 255)
    private String title;

    @Column(length = 1000)
    private String description;
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonBackReference
    private Lesson lesson;

    @Enumerated(EnumType.STRING)
    private FilePurpose filePurpose;
}
