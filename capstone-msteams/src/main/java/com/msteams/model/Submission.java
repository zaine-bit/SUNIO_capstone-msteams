package com.msteams.model;

/**
 * Matches the "submissions" table. Belongs to one Assignment and one Student
 * (by id -- no object references, since these are loaded independently from the DB).
 */
public class Submission {
    private int submissionId;
    private final int studentId;
    private final int assignmentId;
    private final String studentName; // denormalized for display in tables/lists
    private final String fileUrl;
    private Double grade; // null until graded

    public Submission(int submissionId, int studentId, int assignmentId, String studentName, String fileUrl, Double grade) {
        this.submissionId = submissionId;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.studentName = studentName;
        this.fileUrl = fileUrl;
        this.grade = grade;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public boolean isGraded() {
        return grade != null;
    }

    @Override
    public String toString() {
        String gradeText = isGraded() ? String.valueOf(grade) : "Not graded";
        return studentName + " - " + fileUrl + " (" + gradeText + ")";
    }
}
