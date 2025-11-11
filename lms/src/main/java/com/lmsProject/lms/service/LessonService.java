package com.lmsProject.lms.service;

import java.util.List;

import com.lmsProject.lms.entity.Lesson;

public interface LessonService {
    Lesson createLesson(Lesson lesson,Long courseId);
    Lesson updateLesson(Long id, Lesson lesson);
    Lesson getLessonById(Long id);
    List<Lesson> getAllLessons();
    void deleteLesson(Long id);
    List<Lesson> searchLessons(String keyword);
    List<Lesson> getLessonsByCourseId(Long courseId);    
}