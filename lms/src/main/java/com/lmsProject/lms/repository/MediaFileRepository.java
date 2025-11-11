package com.lmsProject.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmsProject.lms.entity.MediaFile;
import com.lmsProject.lms.enums.FilePurpose;

public interface MediaFileRepository extends JpaRepository<MediaFile,Long>{
    List<MediaFile> findByLessonId(Long lessonId);
    List<MediaFile> findByLessonIdAndFilePurpose(Long lessonId, FilePurpose filePurpose);
}
