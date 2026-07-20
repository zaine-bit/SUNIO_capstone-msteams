package com.msteams.service;

import com.msteams.model.Assignment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Full CRUD for the "assignments" table (Create/Read/Update/Delete),
 * satisfying the "ability to CRUD to those tables" requirement.
 */
public class AssignmentDAO {

    // CREATE
    public boolean create(String title, String description, LocalDate dueDate, int teacherId) {
        String sql = "INSERT INTO assignments (title, description, due_date, teacher_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setDate(3, Date.valueOf(dueDate));
            stmt.setInt(4, teacherId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (all)
    public List<Assignment> getAll() {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignments ORDER BY due_date";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                assignments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    // UPDATE
    public boolean update(int assignmentId, String title, String description, LocalDate dueDate) {
        String sql = "UPDATE assignments SET title = ?, description = ?, due_date = ? WHERE assignment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setDate(3, Date.valueOf(dueDate));
            stmt.setInt(4, assignmentId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean delete(int assignmentId) {
        // Submissions reference this assignment via a foreign key, so they
        // must be deleted first or the delete will fail with a constraint error.
        String deleteSubmissions = "DELETE FROM submissions WHERE assignment_id = ?";
        String deleteAssignment = "DELETE FROM assignments WHERE assignment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteSubmissions)) {
                stmt1.setInt(1, assignmentId);
                stmt1.executeUpdate();
            }
            try (PreparedStatement stmt2 = conn.prepareStatement(deleteAssignment)) {
                stmt2.setInt(1, assignmentId);
                stmt2.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Assignment mapRow(ResultSet rs) throws SQLException {
        return new Assignment(
                rs.getInt("assignment_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("due_date").toLocalDate(),
                rs.getInt("teacher_id")
        );
    }
}
