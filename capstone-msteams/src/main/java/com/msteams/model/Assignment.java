package com.msteams.model;

import java.time.LocalDate;

/**
 * Matches the "assignments" table. No submissions list here anymore --
 * SubmissionDAO fetches submissions for a given assignment directly from the DB.
 */
public class Assignment {
    private int assignmentId;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final int teacherId;

    public Assignment(int assignmentId, String title, String description, LocalDate dueDate, int teacherId) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.teacherId = teacherId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getTeacherId() {
        return teacherId;
    }

    @Override
    public String toString() {
        return title + " (due " + dueDate + ")";
    }
}
