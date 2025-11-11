package com.lmsProject.lms.service;

import java.util.List;
import java.util.Map;

import com.lmsProject.lms.entity.TaskSubmission;

public interface TaskSubmissionService {

    TaskSubmission submitTask(TaskSubmission taskSubmission, Long studentId, Long taskId);

    List<TaskSubmission> getSubmissionsByTask(Long taskId);

    List<TaskSubmission> getSubmissionsByStudent(Long studentId);

    TaskSubmission getSubmissionById(Long submissionId);
    
    TaskSubmission gradeSubmission(Long submissionId, Integer marks, String feedback);

    List<TaskSubmission> getPendingSubmissionsForInstructor(Long instructorId);

    TaskSubmission getByTaskId(Long taskId);

    TaskSubmission getSubmissionWithLesson(Long id);

    Map<Long,Boolean> getSubmissionStatusForStudent(Long studentId, List<Long> taskIds);

    boolean existsByTaskIdAndStudentId(Long id, Long studentId);
}
