package com.msteams.service;

import com.msteams.model.Submission;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Full CRUD for the "submissions" table.
 */
public class SubmissionDAO {

    // CREATE
    public boolean create(int studentId, int assignmentId, String fileUrl) {
        String sql = "INSERT INTO submissions (student_id, assignment_id, file_url) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, assignmentId);
            stmt.setString(3, fileUrl);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (all submissions for one assignment, with the student's name joined in)
    public List<Submission> getByAssignment(int assignmentId) {
        List<Submission> submissions = new ArrayList<>();
        String sql = "SELECT s.*, u.name AS student_name " +
                "FROM submissions s JOIN users u ON s.student_id = u.user_id " +
                "WHERE s.assignment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, assignmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    submissions.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    // READ (whether a given student has already submitted a given assignment)
    public Submission getByStudentAndAssignment(int studentId, int assignmentId) {
        String sql = "SELECT s.*, u.name AS student_name " +
                "FROM submissions s JOIN users u ON s.student_id = u.user_id " +
                "WHERE s.student_id = ? AND s.assignment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, assignmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE (grading)
    public boolean updateGrade(int submissionId, double grade) {
        String sql = "UPDATE submissions SET grade = ? WHERE submission_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, grade);
            stmt.setInt(2, submissionId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean delete(int submissionId) {
        String sql = "DELETE FROM submissions WHERE submission_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, submissionId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Submission mapRow(ResultSet rs) throws SQLException {
        double gradeValue = rs.getDouble("grade");
        Double grade = rs.wasNull() ? null : gradeValue;

        return new Submission(
                rs.getInt("submission_id"),
                rs.getInt("student_id"),
                rs.getInt("assignment_id"),
                rs.getString("student_name"),
                rs.getString("file_url"),
                grade
        );
    }
}
