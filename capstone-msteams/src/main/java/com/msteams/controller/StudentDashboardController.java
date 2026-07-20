package com.msteams.controller;

import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentDashboardController {

    @FXML private Label welcomeLabel;

    private final ISessionManager sessionManager = new FileSessionManager();

    @FXML
    public void initialize() {
        // Reads session.dat to find out who's logged in -- this is the
        // "use the serialized file to validate and maintain the session
        // while navigating the system" part.
        var user = sessionManager.loadSession();
        if (user == null) {
            redirectToLogin();
            return;
        }
        welcomeLabel.setText("Welcome, " + user.getName());
    }

    @FXML
    private void goToAssignments() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "assignment_list.fxml");
    }

    @FXML
    private void goToChannel() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "channel.fxml");
    }

    @FXML
    private void logOut() throws IOException {
        // Deletes session.dat and sends the user back to the login screen.
        sessionManager.clearSession();
        FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "login.fxml");
    }

    private void redirectToLogin() {
        try {
            FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
