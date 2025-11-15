package com.lmsProject.lms.service.impl;

import java.io.IOException;
import java.lang.StackWalker.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lmsProject.lms.entity.Lesson;
import com.lmsProject.lms.entity.MediaFile;
import com.lmsProject.lms.entity.TaskSubmission;
import com.lmsProject.lms.enums.FilePurpose;
import com.lmsProject.lms.repository.LessonRepository;
import com.lmsProject.lms.repository.MediaFileRepository;
import com.lmsProject.lms.repository.TaskSubmissionRepository;
import com.lmsProject.lms.service.MediaFileService;

@Service
public class MediaFileServiceImpl implements MediaFileService {
    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Override
    public MediaFile uploadFile(MultipartFile file, String title, String description, Long lessonId, FilePurpose filePurpose) {
        try {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            MediaFile mediaFile = new MediaFile();
            mediaFile.setFileName(fileName);
            mediaFile.setFileUrl("uploads/" + fileName); // Store relative path
            mediaFile.setFileType(file.getContentType());
            mediaFile.setFileSize(file.getSize());
            mediaFile.setUploadedAt(LocalDateTime.now());
            mediaFile.setTitle(title);
            mediaFile.setDescription(description);
            mediaFile.setFilePurpose(filePurpose);
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
            mediaFile.setLesson(lesson);
            return mediaFileRepository.save(mediaFile);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    @Override
    public List<MediaFile> getAllMediaFiles() {
        return mediaFileRepository.findAll();
    }

    /*
     * @Override
     * public List<MediaFile> getAllMediaFilesByLesson(Long lessonId) {
     * // TODO Auto-generated method stub
     * throw new
     * UnsupportedOperationException("Unimplemented method 'getAllMediaFilesByLesson'"
     * );
     * }
     */

    @Override
    public MediaFile getMediaFileById(Long id) {
        return mediaFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media file not found"));
    }

    @Override
    public void deleteMediaFile(Long id) {
        Optional<MediaFile> mediaFileOpt = mediaFileRepository.findById(id);
        if (mediaFileOpt.isPresent()) {
            MediaFile mediaFile = mediaFileOpt.get();
            Optional<TaskSubmission> task = taskSubmissionRepository.findByTaskIdIfPresent(id);
            if(task.isPresent()){
                taskSubmissionRepository.deleteById(task.get().getId());
            }
            try {
                Files.deleteIfExists(Paths.get(mediaFile.getFileUrl()));
            } catch (IOException e) {
                System.out.println("Warning: Unable to delete file from storage");
            }
            mediaFileRepository.delete(mediaFile);
        } else {
            throw new RuntimeException("Media file not found");
        }

    }

    @Override
    public List<MediaFile> getMediaFilesByLessonId(Long lessonId) {
        return mediaFileRepository.findByLessonId(lessonId);
    }

    @Override
    public List<MediaFile> getMediaFilesByPurpose(Long lessonId, FilePurpose filePurpose) {
        return mediaFileRepository.findByLessonIdAndFilePurpose(lessonId, filePurpose);
    }

}
