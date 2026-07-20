package com.msteams.service;

import com.msteams.model.Channel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD for the "channels" table. The app only actively uses one seeded
 * channel, but Create/Read/Delete are here for completeness.
 */
public class ChannelDAO {

    public boolean create(String name) {
        String sql = "INSERT INTO channels (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Channel> getAll() {
        List<Channel> channels = new ArrayList<>();
        String sql = "SELECT * FROM channels";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                channels.add(new Channel(rs.getInt("channel_id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    public boolean delete(int channelId) {
        String deleteMessages = "DELETE FROM messages WHERE channel_id = ?";
        String deleteChannel = "DELETE FROM channels WHERE channel_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteMessages)) {
                stmt1.setInt(1, channelId);
                stmt1.executeUpdate();
            }
            try (PreparedStatement stmt2 = conn.prepareStatement(deleteChannel)) {
                stmt2.setInt(1, channelId);
                stmt2.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
