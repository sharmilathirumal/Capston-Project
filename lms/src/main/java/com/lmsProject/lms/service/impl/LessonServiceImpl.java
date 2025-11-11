package com.lmsProject.lms.service.impl;

import com.lmsProject.lms.entity.Course;
import com.lmsProject.lms.entity.Lesson;
import com.lmsProject.lms.repository.LessonRepository;
import com.lmsProject.lms.service.CourseService;
import com.lmsProject.lms.service.LessonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseService courseService;

    @Override
    public Lesson createLesson(Lesson lesson, Long courseId) {
        Course course = courseService.getCourseById(courseId);
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Long id, Lesson updatedLesson) {
        Lesson existing = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        existing.setTitle(updatedLesson.getTitle());
        existing.setDescription(updatedLesson.getDescription());
        existing.setOrderNumber(updatedLesson.getOrderNumber());
        existing.setLessonType(updatedLesson.getLessonType());
        existing.setContentUrl(updatedLesson.getContentUrl());
        existing.setTextContent(updatedLesson.getTextContent());
        existing.setPublished(updatedLesson.isPublished());
        existing.setLessonDate(updatedLesson.getLessonDate());

        return lessonRepository.save(existing);
    }

    @Override
    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public List<Lesson> searchLessons(String keyword) {
        return lessonRepository.searchLessons(keyword);
    }

    @Override
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderNumberAsc(courseId);
    }
}
