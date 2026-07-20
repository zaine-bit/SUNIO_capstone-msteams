package com.msteams.service;

import com.msteams.model.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Create/Read/Delete for the "messages" table (validation of blank content
 * happens in the controller before create() is called, matching the
 * activity diagram's "validate message" decision).
 */
public class MessageDAO {

    // CREATE
    public boolean create(int channelId, int authorId, String content) {
        String sql = "INSERT INTO messages (channel_id, author_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, channelId);
            stmt.setInt(2, authorId);
            stmt.setString(3, content);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (all messages in a channel, newest last, with author name joined in)
    public List<Message> getByChannel(int channelId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT m.*, u.name AS author_name " +
                "FROM messages m JOIN users u ON m.author_id = u.user_id " +
                "WHERE m.channel_id = ? ORDER BY m.timestamp";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, channelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // DELETE (only meant to be called for the current user's own message --
    // the controller checks authorId before allowing this)
    public boolean delete(int messageId) {
        String sql = "DELETE FROM messages WHERE message_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, messageId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Message mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("timestamp");
        LocalDateTime timestamp = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

        return new Message(
                rs.getInt("message_id"),
                rs.getInt("channel_id"),
                rs.getInt("author_id"),
                rs.getString("author_name"),
                rs.getString("content"),
                timestamp
        );
    }
}
