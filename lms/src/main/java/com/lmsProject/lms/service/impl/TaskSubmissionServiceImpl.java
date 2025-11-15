package com.lmsProject.lms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lmsProject.lms.entity.*;
import com.lmsProject.lms.repository.*;
import com.lmsProject.lms.service.TaskSubmissionService;

@Service
public class TaskSubmissionServiceImpl implements TaskSubmissionService {

    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public TaskSubmission submitTask(TaskSubmission taskSubmission, Long studentId, Long taskId) {
        Long instructorId = taskSubmissionRepository.findInstructorIdBySubmissionId(taskId);

        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MediaFile task = mediaFileRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskSubmission.setStudent(student);
        taskSubmission.setTask(task);
        taskSubmission.setSubmittedAt(java.time.LocalDateTime.now());
        taskSubmission.setInstructor(instructor);
        return taskSubmissionRepository.save(taskSubmission);
    }

    @Override
    public List<TaskSubmission> getSubmissionsByTask(Long taskId) {
        return null;
    }

    @Override
    public List<TaskSubmission> getSubmissionsByStudent(Long studentId) {
        return taskSubmissionRepository.findByStudentId(studentId);
    }

    @Override
    public TaskSubmission gradeSubmission(Long submissionId, Integer marks, String feedback) {
        TaskSubmission submission = taskSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setMarks(marks);
        submission.setFeedback(feedback);
        return taskSubmissionRepository.save(submission);
    }

    @Override
    public List<TaskSubmission> getPendingSubmissionsForInstructor(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        return taskSubmissionRepository.getPendingSubmissionsForInstructor(instructor.getId());
    }

    @Override
    public TaskSubmission getSubmissionById(Long submissionId) {
        return taskSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    @Override
    public TaskSubmission getByTaskId(Long taskId) {
        return taskSubmissionRepository.findByTaskId(taskId);
    }

    public TaskSubmission getSubmissionWithLesson(Long id) {
        return taskSubmissionRepository.findWithLesson(id);
    }

    @Override
    public Map<Long, Boolean> getSubmissionStatusForStudent(Long studentId, List<Long> taskIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return taskIds.stream().collect(
                java.util.stream.Collectors.toMap(
                        taskId -> taskId,
                        taskId -> taskSubmissionRepository.existsByTaskAndStudent(
                                mediaFileRepository.findById(taskId)
                                        .orElseThrow(() -> new RuntimeException("Task not found")),
                                student
                        )
                )
        );
    }

    @Override
    public boolean existsByTaskIdAndStudentId(Long id, Long studentId) {
        return taskSubmissionRepository.existsByTaskAndStudent(
                mediaFileRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Task not found")),
                studentRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"))
        );
    }

    public List<TaskSubmission> getAllPendingTasksOfInstructor(Long id){
      return  taskSubmissionRepository.getPendingSubmissionsForInstructor(id);
    }
    
}
