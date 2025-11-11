    package com.lmsProject.lms.entity;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import com.lmsProject.lms.enums.LessonType;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Lesson {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String title;

        @Column(length = 1000)
        private String description;

        private int orderNumber;

        @Enumerated(EnumType.STRING)
        private LessonType lessonType = LessonType.TEXT;

        private String contentUrl;

        @Column(length = 5000)
        private String textContent;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime lessonDate = LocalDateTime.now();

        private boolean isPublished = true;

        @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<MediaFile> mediaFiles = new ArrayList<>();
        
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "course_id")
        private Course course;

    }
