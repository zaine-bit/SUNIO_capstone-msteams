package com.msteams.controller;

import com.msteams.model.Channel;
import com.msteams.model.Message;
import com.msteams.model.Teacher;
import com.msteams.model.User;
import com.msteams.service.ChannelDAO;
import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import com.msteams.service.MessageDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Implements the "Post Message in Channel" flow: type -> validate (not blank)
 * -> save -> refresh view. Also implements Delete for the user's own messages,
 * so the messages table has Create, Read, and Delete covered.
 */
public class ChannelController {

    @FXML private Label channelNameLabel;
    @FXML private ListView<Message> messageListView;
    @FXML private TextField messageField;
    @FXML private Label statusLabel;

    private Channel channel;
    private User currentUser;
    private final ChannelDAO channelDAO = new ChannelDAO();
    private final MessageDAO messageDAO = new MessageDAO();
    private final ISessionManager sessionManager = new FileSessionManager();

    @FXML
    public void initialize() {
        currentUser = sessionManager.loadSession();

        List<Channel> channels = channelDAO.getAll();
        if (channels.isEmpty()) {
            statusLabel.setText("No channel found. Run the SQL schema/seed first.");
            return;
        }
        channel = channels.get(0); // using the first (seeded "General") channel
        channelNameLabel.setText(channel.getName());
        refreshMessages();
    }

    @FXML
    private void handleSend() {
        String content = messageField.getText();
        if (content == null || content.isBlank()) {
            statusLabel.setText("Message can't be empty.");
            return;
        }

        boolean posted = messageDAO.create(channel.getChannelId(), currentUser.getUserId(), content);

        if (posted) {
            statusLabel.setText("");
            messageField.clear();
            refreshMessages();
        } else {
            statusLabel.setText("Failed to send message.");
        }
    }

    @FXML
    private void handleDelete() {
        Message selected = messageListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a message to delete.");
            return;
        }

        if (selected.getAuthorId() != currentUser.getUserId()) {
            statusLabel.setText("You can only delete your own messages.");
            return;
        }

        boolean deleted = messageDAO.delete(selected.getMessageId());
        statusLabel.setText(deleted ? "Message deleted." : "Failed to delete message.");
        if (deleted) {
            refreshMessages();
        }
    }

    private void refreshMessages() {
        messageListView.getItems().setAll(messageDAO.getByChannel(channel.getChannelId()));
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) channelNameLabel.getScene().getWindow();
        if (currentUser instanceof Teacher) {
            FXMLLoaderHelper.switchScene(stage, "teacher_dashboard.fxml");
        } else {
            FXMLLoaderHelper.switchScene(stage, "student_dashboard.fxml");
        }
    }
}
