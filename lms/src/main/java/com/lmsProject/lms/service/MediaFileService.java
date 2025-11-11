package com.lmsProject.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lmsProject.lms.entity.MediaFile;
import com.lmsProject.lms.enums.FilePurpose;
import com.lmsProject.lms.repository.MediaFileRepository;

public interface MediaFileService {

    MediaFile uploadFile(MultipartFile file,String title,String description,Long lessonId,FilePurpose filePurpose);

    // List<MediaFile> getAllMediaFilesByLesson(Long lessonId);

    List<MediaFile> getAllMediaFiles();

    MediaFile getMediaFileById(Long id);

    void deleteMediaFile(Long id);

    List<MediaFile> getMediaFilesByLessonId(Long lessonId);

    List<MediaFile> getMediaFilesByPurpose(Long lessonId, FilePurpose filePurpose);

}
