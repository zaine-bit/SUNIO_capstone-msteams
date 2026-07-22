package com.msteams.service;

import com.msteams.model.Assignment;

import java.time.LocalDate;

// Facade pattern: hides deadline/file/grade validation plus DAO calls behind two simple methods.
public class ClassroomFacade {

    private final SubmissionDAO submissionDAO = new SubmissionDAO();

    public Result submitAssignment(int studentId, Assignment assignment, String fileName) {
        if (LocalDate.now().isAfter(assignment.getDueDate())) {
            return new Result(false, "Submission closed: the deadline for this assignment has passed.");
        }
        if (fileName == null || fileName.isBlank()) {
            return new Result(false, "Please attach a file before submitting.");
        }
        boolean success = submissionDAO.create(studentId, assignment.getAssignmentId(), fileName);
        return success
                ? new Result(true, "Submission successful! Your file \"" + fileName + "\" was submitted.")
                : new Result(false, "Something went wrong saving your submission. Please try again.");
    }

    public Result gradeSubmission(int submissionId, String gradeText) {
        double grade;
        try {
            grade = Double.parseDouble(gradeText.trim());
        } catch (NumberFormatException e) {
            return new Result(false, "Grade must be a number.");
        }
        boolean success = submissionDAO.updateGrade(submissionId, grade);
        return success ? new Result(true, "Grade saved.") : new Result(false, "Failed to save grade.");
    }

    // Simple outcome wrapper so controllers don't need try/catch or DAO details.
    public static class Result {
        public final boolean success;
        public final String message;

        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
